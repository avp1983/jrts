package com.bssys.ejb;

import com.bssys.entity.PayLegal.UmtPayLegal;
import com.bssys.entity.UMTDictSettings;
import com.bssys.entity.UmtTransferClient;
import com.google.common.base.Joiner;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.MapAttribute;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by e.zemtcovsky on 19.02.2015.
 */

@Stateless
@Named
public class UMTDictSettingsFacade {
  @PersistenceContext
  private transient EntityManager em;

  public UmtTransferClient findClientByPayLegalSender(UmtPayLegal payLegal){
    List<UMTDictSettings> dictSettingsList = em.createNamedQuery("UMTDictSettings.OneWinClientUniqueFields", UMTDictSettings.class).getResultList();
    if (dictSettingsList.isEmpty()) {
      return null;
    }
    String str = getStringQuery(dictSettingsList);
    TypedQuery<UmtTransferClient> query = em.createQuery(str, UmtTransferClient.class);

    for (UMTDictSettings dictSetting : dictSettingsList){
      query.setParameter(dictSetting.getFieldname(), getPaylegalFieldValue(payLegal, dictSetting.getFieldname()));
    }
    try {
      return query.getSingleResult();
    }
    catch (NoResultException e){
      return null;
    }
  }

  public boolean senderFieldsFilled(UmtPayLegal payLegal){
    List<UMTDictSettings> dictSettingsList = em.createNamedQuery("UMTDictSettings.OneWinClientMandatoryFields", UMTDictSettings.class).getResultList();
    if (dictSettingsList.isEmpty()) {
      return true;
    }
    for (UMTDictSettings dictSetting : dictSettingsList){
      Object paylegalFieldValue = getPaylegalFieldValue(payLegal, dictSetting.getFieldname());
      if ((paylegalFieldValue == null) || paylegalFieldValue.toString().isEmpty()) {
        return false;
      }
    }
    return true;
  }

  private String getStringQuery(List<UMTDictSettings> dictSettingsList) {
    List<String> list = new ArrayList<String>();
    for (UMTDictSettings dictSetting : dictSettingsList) {
      String fieldName = dictSetting.getFieldname();
      list.add("cl." + getClientFieldName(fieldName) + "=:" + fieldName);
    }
    return "from UmtTransferClient cl where " +  Joiner.on(" and ").join(list);
  }

  private String getClientFieldName(String fieldName) {
    switch (fieldName.toUpperCase()){
      case "FIRSTNAME": return "firstName";
      case "LASTNAME" : return "lastName";
      case "PATRONYMIC": return "patronymic";
      case "MIGRATIONCARDNUMBER": return "migrationCardNumber";
      case "ISRESIDENT ": return "isResident";
      case "NATIONALITY_CODE": return "nationalityCode";
      case "LICENCETYPE": return "licenceType";
      case "LICENCESERIES": return "licenceSeries";
      case "LICENCENUMBER": return "licenceNumber";
      case "LICENCEISSUER": return "licenceIssuer";
      case "LICENCEDATEISSUE": return "licenceDateIssue";
      case "COUNTRYISO": return "countryISO";
      case "CITY": return "city";
      case "STREET": return "street";
      case "HOUSE": return "house";
      case "HOUSEBLOCK": return "houseBlock";
      case "HOUSEBUILDING": return "houseBuilding";
      case "FLAT": return "flat";
      case "PHONE": return "phone";
      case "EMAIL": return "email";
      case "INN": return "iNN";
      case "BIRTHDAY": return "birthday";
      case "BIRTHPLACE": return "birthPlace";
    }
    return null;
  }

  private Object getPaylegalFieldValue(UmtPayLegal payLegal, String fieldName){

    // скажи нет рефлексии :)
    switch (fieldName.toUpperCase()){
      case "FIRSTNAME": return payLegal.getSenderFirstName();
      case "LASTNAME" : return payLegal.getSenderLastName();
      case "PATRONYMIC": return payLegal.getSenderPatronymic();
      case "MIGRATIONCARDNUMBER": return payLegal.getSenderMigrationCardNumber();
      case "ISRESIDENT": return payLegal.getSenderIsResident();
      case "NATIONALITY_CODE": return payLegal.getSenderNationalityCode();
      case "LICENCETYPE": return payLegal.getSenderLicenceType();
      case "LICENCESERIES": return payLegal.getSenderLicenceSeries();
      case "LICENCENUMBER": return payLegal.getSenderLicenceNumber();
      case "LICENCEISSUER": return payLegal.getSenderLicenceIssuer();
      case "LICENCEDATEISSUE": return payLegal.getSenderLicenceDateIssue();
      case "COUNTRYISO": return payLegal.getSenderCountryISO();
      case "CITY": return payLegal.getSenderCity();
      case "STREET": return payLegal.getSenderStreet();
      case "HOUSE": return payLegal.getSenderHouse();
      case "HOUSEBLOCK": return payLegal.getSenderHouseBlock();
      case "HOUSEBUILDING": return payLegal.getSenderHouseBuilding();
      case "FLAT": return payLegal.getSenderFlat();
      case "PHONE": return payLegal.getSenderPhone();
      case "EMAIL": return payLegal.getSenderEmail();
      case "INN": return payLegal.getSenderINN();
      case "BIRTHDAY": return payLegal.getSenderBirthday();
      case "BIRTHPLACE": return payLegal.getSenderBirthPlace();
    }

    return null;
  }

}
