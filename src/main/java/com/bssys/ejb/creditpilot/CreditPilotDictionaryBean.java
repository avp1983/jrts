package com.bssys.ejb.creditpilot;


import com.bssys.ejb.AdminNotificationBean;
import com.bssys.entity.UMTSettingsConst;
import com.bssys.entity.UMTWebProvBalanceList;
import com.bssys.entity.UMTWebProvCategories;
import com.bssys.entity.UMTWebProvMessage;
import com.bssys.entity.UMTWebProvMessageList;
import com.bssys.entity.UMTWebProvider;
import com.bssys.entity.UMTWebProviderParam;
import com.bssys.process.step.ProcessStepResult;
import com.bssys.process.step.ProcessStepResultType;
import com.bssys.umt.UpdateException;
import org.xml.sax.SAXException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bssys.ejb.creditpilot.ActionType.ACCOUNT;
import static com.bssys.ejb.creditpilot.ActionType.MESSAGES;
import static com.bssys.ejb.creditpilot.ActionType.PROVIDERS2;
import static com.bssys.ejb.creditpilot.ActionType.SGROUPS;

@Stateless
@Named
public class CreditPilotDictionaryBean implements Serializable {

  @PersistenceContext
  private transient EntityManager em;

  @Inject
  private CreditPilotSettingsFacade settingsFacade;
  @Inject
  private CreditPilotFacade creditPilotFacade;
  @Inject
  private AdminNotificationBean adminNotificationBean;
  @Inject
  private CreditPilotDictionaryInserterBean dictionaryInserterBean;


  public String loadBalance() throws Exception {
    creditPilotFacade.checkGateEnabled();

    Response response = creditPilotFacade.getResponse(ACCOUNT, new HashMap<String, String>());
    try {
      UMTWebProvBalanceList balanceList = response.readEntity(UMTWebProvBalanceList.class);
      return String.valueOf(balanceList.getAccounts().get(0).getBalance());
    } finally {
      response.close();
    }
  }

  public String loadMessages() throws Exception {
    creditPilotFacade.checkGateEnabled();

    UMTSettingsConst dateUpdate = settingsFacade.getLastMsgUpdate();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy%20HH:mm:ss");
    Map<String, String> appendParams = new HashMap<>();
    appendParams.put("fromDate", dateFormat.format(dateUpdate.getDateTimeValue()));
    dateUpdate.setDateTimeValue(new Date());

    Response response = creditPilotFacade.getResponse(MESSAGES, appendParams);
    try {
      UMTWebProvMessageList messageList = response.readEntity(UMTWebProvMessageList.class);

      // Обновляем сообщения провайдеров
      if (messageList.getMessages() != null) {
        for (UMTWebProvMessage message : messageList.getMessages()) {
          if (message.getProviderID() != null) {
            UMTWebProvider provider = em.find(UMTWebProvider.class, message.getProviderID());
            if (provider != null) {
              if (message.getType().equals("RESUME")) {
                provider.setInfoMessage(null);
              } else {
                provider.setInfoMessage(message.getText());
              }
              em.merge(provider);
            }
          }
        }
        em.merge(dateUpdate);
        return String.valueOf(messageList.getMessages().size());
      } else {
        em.merge(dateUpdate);
        return "0";
      }
    } finally {
      response.close();
    }
  }

  public <T> List<T> readCollectionFromStream(InputStream stream, String xmlTagName, Class<T> tClass, StringBuilder errors) throws JAXBException, XMLStreamException, IOException, SAXException {
    final QName qName = new QName(xmlTagName);
    List<T> resultList = new java.util.ArrayList<>();

    XMLInputFactory xif = XMLInputFactory.newInstance();
    XMLEventReader reader = xif.createXMLEventReader(stream);

    JAXBContext jaxbContext = JAXBContext.newInstance(tClass);
    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

    ParserValidator validator = new ParserValidator();
    unmarshaller.setEventHandler(validator);

    XMLEvent e;
    while ((e = reader.peek()) != null) {
      if (e.isStartElement() && ((StartElement) e).getName().equals(qName)) {
        try {
          T element = unmarshaller.unmarshal(reader, tClass).getValue();
          resultList.add(element);
        } catch (Exception ex) {
          reader.next();
        }
      } else {
        reader.next();
      }
    }
    errors.append(validator.getSb().toString());

    return resultList;
  }

  /**
   * Загрузка справочника провайдеров КредитПилот
   * @return Количество обработанных записей
   * @throws Exception
   */
  public String loadProviders() throws Exception {
    creditPilotFacade.checkGateEnabled();
    StringBuilder errors = new StringBuilder();

    int countAll;
    int countUpdated = 0;
    try (InputStream response = creditPilotFacade.getResponseStream(PROVIDERS2, new HashMap<String, String>())) {
      List<UMTWebProvider> providers = readCollectionFromStream(response, "provider", UMTWebProvider.class, errors);
      countAll = providers.size();

      // Помечаем все записи в базе как неактивные,
      // оставшиеся неактивными после обновления записи будут удалены
      dictionaryInserterBean.setProvidersPreUpdate();

      for (UMTWebProvider provider : providers) {
        prepareProviderParameters(provider);
        try {
          dictionaryInserterBean.saveProvider(provider);
          countUpdated++;
        } catch (UpdateException e) {
          errors.append(String.format("Ошибка при импорте провайдера с идентификатором %d: %s\n\n", provider.getID(), e.getMessage()));
        }
      }

      dictionaryInserterBean.setProvidersAfterUpdate();

      return String.format("updated %d of %d\nerrors\n%s", countUpdated, countAll, errors.toString());
    } finally {
      // Отправляем сообщение администратору если при загрузке возникли ошибки
      if (errors.length() > 0) {
        ProcessStepResult checkResponseResult = new ProcessStepResult(
            errors.toString(),
            ProcessStepResultType.PROCESS_RESULT_ERROR, "Ошибки при загрузке справочника провайдеров");
        adminNotificationBean.sendEmailToAdmin(checkResponseResult);
      }

    }
  }

  public void prepareProviderParameters(UMTWebProvider provider) {
    TypedQuery<UMTWebProviderParam> query =
        em.createQuery("select p from UMTWebProviderParam p where p.provider = :providerID", UMTWebProviderParam.class);
    query.setParameter("providerID", provider);

    for (UMTWebProviderParam loadedParam : provider.getParams()) {
      if (loadedParam.getType() == null) {
        loadedParam.setType("text");
      }
    }

    for (UMTWebProviderParam param : query.getResultList()) {
      for (UMTWebProviderParam loadedParam : provider.getParams()) {
        if ((param.getName() == null) && (loadedParam.getName() == null)) {
          loadedParam.setAutoKey(param.getAutoKey());
        } else if (((param.getName() != null)) && (param.getName().equals(loadedParam.getName()))) {
          loadedParam.setAutoKey(param.getAutoKey());
        }
      }
    }
  }

  /**
   * Загрузка справочника категорий
   * @return Количество обработанных записей
   * @throws Exception
   */
  public String loadCategories() throws Exception {
    creditPilotFacade.checkGateEnabled();

    int countAll;
    int countUpdated = 0;
    StringBuilder errors = new StringBuilder();
    try (InputStream response = creditPilotFacade.getResponseStream(SGROUPS, new HashMap<String, String>())) {
      List<UMTWebProvCategories> categoriesList = readCollectionFromStream(response, "serviceProviderGroup", UMTWebProvCategories.class, errors);
      countAll = categoriesList.size();

      dictionaryInserterBean.setCategoriesPreUpdate();
      for (UMTWebProvCategories category : categoriesList) {
        try {
          dictionaryInserterBean.saveCategory(category);
          countUpdated++;
        } catch (UpdateException e) {
          errors.append(String.format("Ошибка при импорте категории с идентификатором %d: %s\n\n",
              category.getID(), e.getMessage()));
        }
      }
      dictionaryInserterBean.setCategoriesAfterUpdate();

      return String.format("updated %d of %d\nerrors\n%s", countUpdated, countAll, errors.toString());
    } finally {
      if (errors.length() > 0) {
        ProcessStepResult checkResponseResult = new ProcessStepResult(
            errors.toString(),
            ProcessStepResultType.PROCESS_RESULT_ERROR, "Ошибки при загрузке справочника категорий");
        adminNotificationBean.sendEmailToAdmin(checkResponseResult);
      }

    }
  }
}
