package com.bssys.api.chara.gate;

import com.bssys.api.types.*;
import com.bssys.ejb.CountryFacade;
import com.bssys.ejb.UmtPayLegalFacade;
import com.bssys.ejb.UmtPayTransferOpenFacade;
import com.bssys.entity.Country;
import com.bssys.entity.PayLegal.UmtPayLegal;
import com.bssys.entity.PayLegal.UmtPayLegalExField;
import com.bssys.entity.PayLegal.UmtPayLegalExFields;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

@Singleton
@Named
public class ApiDocumentConverter {

  @Inject
  private CountryFacade countryFacade;

  @Inject
  private UmtPayLegalFacade payLegalFacade;

  @Inject
  private UmtPayTransferOpenFacade transferFacade;

  public ApiDocumentConverter() {
  }

  private String getNullStr(String value) {
    if ("".equals(value)) {
      return null;
    }
    return value;
  }

  public static Document getDocumentWithHeaders(Document request) {
    Document document = new Document();
    document.setGuid(request.getGuid());
    document.setPartnerId(request.getPartnerId());
    document.setMemberId(request.getMemberId());
    document.setSystem(request.getSystem());
    document.setDatetime(request.getDatetime());
    document.setVersion(request.getVersion());
    return document;
  }

  private void parsePayLegalCommonInfo(TransferLegalSend document, UmtPayLegal payLegal) {
    if (document.getNumber() != null) {
      payLegal.setDocumentNumber(String.valueOf(document.getNumber()));
    }
    if (document.getAmount() != null) {
      payLegal.setAmount(document.getAmount().floatValue());
    }
    payLegal.setCurrCodeIso(document.getCurrencyAlpha());
    payLegal.setNote(document.getNote());
    if (document.getFee() != null) {
      payLegal.setCommission(document.getFee().floatValue());
    }
  }

  private Country getCountryByCode(String numeric) throws ApiException {
    Country country = countryFacade.getByCode(numeric);
    if (country == null) {
      throw new ApiException(ApiExceptionType.API_ERR_POINT_COUNTRY_CODE_NOT_FOUND, numeric);
    }
    return country;
  }

  private void parsePayLegalSenderData(UmtPayLegal payLegal, ClientType.Data data) throws ApiException {
    if (data == null) {
      return;
    }
    payLegal.setSenderLastName(data.getLastname());
    payLegal.setSenderFirstName(data.getFirstname());
    payLegal.setSenderPatronymic(data.getPatronymic());
    payLegal.setSenderAccount(data.getAccount());
    payLegal.setSenderIsResident(data.getIsresident());
    payLegal.setSenderCardNumber(data.getIdCard());
    if (data.getCountry() != null) {
      payLegal.setSenderCountryISO(getCountryByCode(data.getCountry().getNumeric()));
    }
  }

  private void parsePayLegalSenderAddress(UmtPayLegal payLegal, ClientType.Address address) throws ApiException {
    if (address == null) {
      return;
    }
    payLegal.setSenderCity(address.getCity());
    payLegal.setSenderStreet(address.getStreet());
    payLegal.setSenderHouse(address.getHouse());
    payLegal.setSenderHouseBlock(address.getHouseBlock());
    payLegal.setSenderHouseBuilding(address.getHouseBuilding());
    payLegal.setSenderFlat(address.getFlat());
    payLegal.setSenderPhone(address.getPhone());
    payLegal.setSenderEmail(address.getEmail());
    if (address.getCountry() != null) {
      payLegal.setLocationCountry(getCountryByCode(address.getCountry().getNumeric()));
    }
  }

  private void parsePayLegalSenderExtendedData(UmtPayLegal payLegal, DataExtendedType extendedType) {
    if (extendedType == null) {
      return;
    }
    payLegal.setSenderINN(extendedType.getInn());
    if (extendedType.getBirthDay() != null) {
      payLegal.setSenderBirthday(extendedType.getBirthDay().toGregorianCalendar().getTime());
    }
    payLegal.setSenderBirthPlace(extendedType.getBirthPlace());
    payLegal.setSenderMigrationCardNumber(extendedType.getMigrationCardNumber());
  }

  private void parsePayLegalSenderIdentification(UmtPayLegal payLegal, IdentificationType identType) {
    if (identType == null) {
      return;
    }
    payLegal.setSenderLicenceType(identType.getType());
    payLegal.setSenderLicenceSeries(identType.getSeries());
    payLegal.setSenderLicenceNumber(identType.getNumber());
    payLegal.setSenderLicenceIssuer(identType.getIssuer());
    if (identType.getIssueDate() != null) {
      payLegal.setSenderLicenceDateIssue(identType.getIssueDate().toGregorianCalendar().getTime());
    }
    payLegal.setSenderLicenceDepCode(identType.getDepCode());
  }

  public UmtPayLegal parseUmtPayLegal(TransferLegalSend document, UmtPayLegal payLegal) throws ApiException {
    try {
      parsePayLegalCommonInfo(document, payLegal);
      if (document.getSender() != null) {
        parsePayLegalSenderData(payLegal, document.getSender().getData());
        parsePayLegalSenderAddress(payLegal, document.getSender().getAddress());
        parsePayLegalSenderExtendedData(payLegal, document.getSender().getDataExtended());
        parsePayLegalSenderIdentification(payLegal, document.getSender().getIdentification());
      }
      return payLegal;
    } catch (Exception e) {
      throw new ApiException(ApiExceptionType.API_ERR_FAULT, e.getMessage());
    }
  }

  /**
   * Загружаем переданные пользователем параметры
   *
   * @param request
   * @return
   * @throws ApiException
   */
  public UmtPayLegalExFields parseExFields(TransferLegalSend request) throws ApiException {
    try {
      TransferLegal.Params params = request.getParams();
      UmtPayLegalExFields exFields = new UmtPayLegalExFields();
      for (TransferLegal.Params.Param param : params.getParam()) {
        UmtPayLegalExField field = new UmtPayLegalExField();
        field.setIsEditable(1);
        field.setExName(param.getName());
        field.setExValue(param.getValue());
        exFields.getRecList().add(field);
      }
      return exFields;
    } catch (Exception e) {
      throw new ApiException(ApiExceptionType.API_ERR_FAULT, e.getMessage());
    }
  }

  public ParamType parseParamType(UmtPayLegalExField exField) {
    ParamType param = new ParamType();
    param.setName(exField.getExName());
    // TODO: implement param type
    param.setType("text");
    param.setTitle(exField.getExTitle());
    // TODO: implement minlength
    // TODO: implement maxlength
    // TODO: implement pattern
    param.setPatterndesc(exField.getDescription());
    // TODO: implement mask
    param.setParamValue(exField.getExValue());
    param.setEditable(exField.getIsEditable());
    return param;
  }

  public Document parseResponsePayLegal(Document request, UmtPayLegal payLegal, UmtPayLegalExFields exFields) {
    Document document = getDocumentWithHeaders(request);
    Document.TransferLegal transferLegal = new Document.TransferLegal();
    TransferLegalConfirm confirm = new TransferLegalConfirm();
    TransferLegalConfirm.Params params = new TransferLegalConfirm.Params();

    confirm.setChecknumber(payLegal.getCheckNumber());
    confirm.setFee(new BigDecimal(Float.toString(payLegal.getCommission())));
    confirm.setStepsLeft(BigInteger.valueOf(0));
    if (payLegal.getStepsLeft() != null) {
      confirm.setStepsLeft(BigInteger.valueOf(payLegal.getStepsLeft()));
    }

    int currentStep = 0;
    if (payLegal.getStepsCount() != null) {
      currentStep = (payLegal.getStepsCount() - payLegal.getStepsLeft()) - 1;
    }
    for (UmtPayLegalExField exField : exFields.getRecList()) {
      if (exField.getStep() > currentStep) {
        ParamType param = parseParamType(exField);
        params.getParam().add(param);
      }
    }
    if (!params.getParam().isEmpty()) {
      confirm.setParams(params);
    }
    transferLegal.setCreateReply(confirm);
    document.setTransferLegal(transferLegal);
    return document;
  }

  public Document parseResponseSendPayLegal(Document request, UmtPayLegal payLegal) {
    Document document = getDocumentWithHeaders(request);
    Document.TransferLegal transferLegal = new Document.TransferLegal();
    TransferConfirm confirm = new TransferConfirm();

    confirm.setChecknumber(payLegal.getCheckNumber());
    confirm.setMessage(null);

    transferLegal.setSendReply(confirm);
    document.setTransferLegal(transferLegal);
    return document;
  }

  public Document parseResponseDeletePayLegal(Document request, UmtPayLegal payLegal) {
    Document document = getDocumentWithHeaders(request);
    Document.TransferLegal transferLegal = new Document.TransferLegal();
    TransferConfirm confirm = new TransferConfirm();

    confirm.setChecknumber(payLegal.getCheckNumber());
    confirm.setMessage(null);

    transferLegal.setDeleteReply(confirm);
    document.setTransferLegal(transferLegal);
    return document;
  }

  private XMLGregorianCalendar getXMLGregorianCalendar(Date date) throws ApiException {
    try {
      if (date == null) {
        return null;
      }

      GregorianCalendar calendar = new GregorianCalendar();
      calendar.setTime(date);
      return DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(date));
    } catch (DatatypeConfigurationException e) {
      throw new ApiException(ApiExceptionType.API_ERR_FAULT, String.format("Ошибка при конвертации даты: %s", e.getMessage()));
    }
  }

  private void parsePayLegalArchiveResponseCommonInfo(UmtPayLegal payLegal, TransferLegalArchive archive) throws ApiException {
    archive.setNumber(BigInteger.valueOf(Long.parseLong(payLegal.getDocumentNumber())));
    archive.setProviderId(payLegal.getProvider().getID());
    if (payLegal.getAmount() != null) {
      archive.setAmount(BigDecimal.valueOf(payLegal.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
    }
    archive.setCurrencyAlpha(payLegal.getCurrCodeIso());
    archive.setNote(payLegal.getNote());
    archive.setChecknumber(payLegal.getCheckNumber());
    if (payLegal.getCommission() != null) {
      archive.setFee(BigDecimal.valueOf(payLegal.getCommission()).setScale(2, BigDecimal.ROUND_HALF_UP));
    }
    archive.setStatusCode(BigInteger.valueOf(payLegal.getStatus()));
    archive.setStatusText(transferFacade.getICStatusDescriptionByStatus(payLegal.getStatus(), new Locale("ru", "RU")));
    archive.setCreatingDate(getXMLGregorianCalendar(payLegal.getValueDateTime()));
  }

  private ClientType.Data parsePayLegalArchiveResponseSenderCommonInfo(UmtPayLegal payLegal) {
    ClientType.Data data = new ClientType.Data();
    data.setLastname(getNullStr(payLegal.getSenderLastName()));
    data.setFirstname(getNullStr(payLegal.getSenderFirstName()));
    data.setPatronymic(getNullStr(payLegal.getSenderPatronymic()));
    data.setAccount(getNullStr(payLegal.getSenderAccount()));
    data.setIsresident(payLegal.getSenderIsResident());
    data.setIdCard(getNullStr(payLegal.getSenderCardNumber()));
    if (payLegal.getTransferIssueCountryCode() != null) {
      CountryType countryType = new CountryType();
      countryType.setNumeric(payLegal.getTransferIssueCountryCode());
      data.setCountry(countryType);
    }
    return data;
  }

  private ClientType.Address parsePayLegalArchiveResponseSenderAddress(UmtPayLegal payLegal) {
    ClientType.Address address = new ClientType.Address();
    address.setCity(getNullStr(payLegal.getSenderCity()));
    address.setStreet(getNullStr(payLegal.getSenderStreet()));
    address.setHouse(getNullStr(payLegal.getSenderHouse()));
    address.setHouseBlock(getNullStr(payLegal.getSenderHouseBlock()));
    address.setHouseBuilding(getNullStr(payLegal.getSenderHouseBuilding()));
    address.setFlat(getNullStr(payLegal.getSenderFlat()));
    address.setPhone(getNullStr(payLegal.getSenderPhone()));
    address.setEmail(getNullStr(payLegal.getSenderEmail()));
    if (payLegal.getSenderCountryISO() != null) {
      CountryType countryType = new CountryType();
      countryType.setNumeric(payLegal.getSenderCountryISO().getCode());
      address.setCountry(countryType);
    }
    return address;
  }

  private DataExtendedType parsePayLegalArchiveResponseSenderExtendedInfo(UmtPayLegal payLegal) throws ApiException {
    DataExtendedType extendedType = new DataExtendedType();
    extendedType.setInn(getNullStr(payLegal.getSenderINN()));
    extendedType.setBirthDay(getXMLGregorianCalendar(payLegal.getSenderBirthday()));
    extendedType.setBirthPlace(getNullStr(payLegal.getSenderBirthPlace()));
    extendedType.setMigrationCardNumber(getNullStr(payLegal.getSenderMigrationCardNumber()));
    return extendedType;
  }

  private IdentificationType parsePayLegalArchiveResponseSenderIdentification(UmtPayLegal payLegal) throws ApiException {
    IdentificationType idType = new IdentificationType();
    idType.setType(getNullStr(payLegal.getSenderLicenceType()));
    idType.setSeries(getNullStr(payLegal.getSenderLicenceSeries()));
    idType.setNumber(getNullStr(payLegal.getSenderLicenceNumber()));
    idType.setIssuer(getNullStr(payLegal.getSenderLicenceIssuer()));
    idType.setIssueDate(getXMLGregorianCalendar(payLegal.getSenderLicenceDateIssue()));
    idType.setDepCode(getNullStr(payLegal.getSenderLicenceDepCode()));
    return idType;
  }

  private TransferLegal.Params parsePayLegalArchiveResponseParams(UmtPayLegal payLegal) {
    TransferLegal.Params params = new TransferLegal.Params();
    List<TransferLegal.Params.Param> paramList = params.getParam();
    UmtPayLegalExFields exFields = payLegalFacade.getExFields(payLegal);
    for (UmtPayLegalExField exField : exFields.getRecList()) {
      TransferLegal.Params.Param param = new TransferLegal.Params.Param();
      param.setName(exField.getExName());
      param.setValue(exField.getExValue());
      paramList.add(param);
    }
    return params;
  }

  private TransferLegalArchive parsePayLegalArchiveResponse(UmtPayLegal payLegal) throws ApiException {
    TransferLegalArchive archive = new TransferLegalArchive();
    parsePayLegalArchiveResponseCommonInfo(payLegal, archive);

    ClientType sender = new ClientType();
    sender.setData(parsePayLegalArchiveResponseSenderCommonInfo(payLegal));
    sender.setAddress(parsePayLegalArchiveResponseSenderAddress(payLegal));
    sender.setDataExtended(parsePayLegalArchiveResponseSenderExtendedInfo(payLegal));
    sender.setIdentification(parsePayLegalArchiveResponseSenderIdentification(payLegal));
    archive.setSender(sender);

    archive.setParams(parsePayLegalArchiveResponseParams(payLegal));

    return archive;
  }

  public Document parseResponseArchivePayLegal(Document request, List<UmtPayLegal> payLegalList) throws ApiException {
    Document.TransferLegal transferLegal = new Document.TransferLegal();
    List<TransferLegalArchive> archives = transferLegal.getArchiveReply();
    for (UmtPayLegal payLegal : payLegalList) {
      archives.add(parsePayLegalArchiveResponse(payLegal));
    }

    Document document = getDocumentWithHeaders(request);
    document.setTransferLegal(transferLegal);
    return document;
  }
}
