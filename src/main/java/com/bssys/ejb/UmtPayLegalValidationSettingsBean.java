package com.bssys.ejb;

import com.bssys.entity.UMTTrSysMandatoryFields;
import com.bssys.entity.UmtDocType;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.EnumSet;
import java.util.List;

import static com.bssys.entity.UmtDocType.PayLegal;
import static com.bssys.umt.TransferSys.TRANSFER_SYS_CREDIT_PILOT;

@Named
@SessionScoped
public class UmtPayLegalValidationSettingsBean implements Serializable {
  @PersistenceContext
  private transient EntityManager em;
  private EnumSet<PayLegalValidationField> requiredFields;

  public boolean isRequiredField(PayLegalValidationField field) {
    return requiredFields.contains(field);
  }

  @PostConstruct
  private void initBean() {
    requiredFields = EnumSet.noneOf(PayLegalValidationField.class);
    for (UMTTrSysMandatoryFields field : getMandatoryFields(TRANSFER_SYS_CREDIT_PILOT, PayLegal)) {
      requiredFields.add(PayLegalValidationField.valueOf(field.getFieldName()));
    }
  }

  private List<UMTTrSysMandatoryFields> getMandatoryFields(int transferSys, UmtDocType docType) {
    return em.createNamedQuery("UMTTrSysMandatoryFields.allBySysIdAndTransferType", UMTTrSysMandatoryFields.class)
        .setParameter("sysId", transferSys).setParameter("transferType", docType.getId()).getResultList();
  }

  public enum PayLegalValidationField {
    Note, SenderAccount, SenderBirthday, SenderBirthPlace, SenderCity, SenderCountryISO, SenderEmail, SenderFirstName,
    SenderFlat, SenderHouse, SenderINN, SenderIsResident, SenderLastName,
    SenderLicenceDateIssue, SenderLicenceIssuer, SenderLicenceDepCode, SenderLicenceNumber, SenderLicenceSeries,
    SenderLicenceType, SenderNationality_Code, SenderPatronymic, SenderPhone,
    SenderStreet, SenderOtherDocType, SenderOtherDocNum, SenderOtherDocDateStart, SenderOtherDocDateFinish,
    SenderPostCode, SenderLicenceExpireDate, SenderHouseBlock, SenderHouseBuilding
  }
}
