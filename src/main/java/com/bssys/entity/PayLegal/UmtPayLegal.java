package com.bssys.entity.PayLegal;

import com.bssys.bls.types.DboRootDocument;
import com.bssys.conversion.Money;
import com.bssys.entity.Country;
import com.bssys.entity.UMTWebProvider;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

import static com.bssys.utils.persistence.JpaMetadataBean.SEQUENCE_GENERATOR_COLUMN_PK;
import static com.bssys.utils.persistence.JpaMetadataBean.SEQUENCE_GENERATOR_COLUMN_VALUE;
import static com.bssys.utils.persistence.JpaMetadataBean.SEQUENCE_GENERATOR_TABLE_NAME;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@NamedNativeQueries({
    @NamedNativeQuery(name = "UMTPayLegal.getDocumentNumberByPK",
        query = "SELECT DocumentNumber FROM UMTPayLegal "
            + "where CLIENT = ?1 and DATECREATE = ?2 and TIMECREATE = ?3"),
    @NamedNativeQuery(name = "UMTPayLegal.getStatusByPK",
        query = "SELECT CAST (Status AS DECIMAL) AS Status FROM UMTPayLegal "
            + "where CLIENT = ?1 and DATECREATE = ?2 and TIMECREATE = ?3"),
})
@NamedQueries({
    @NamedQuery(name = "UMTPayLegal.notFinishedDocs",
        query = "select doc from UmtPayLegal doc where doc.isRemoteOperationComplete = 0"),
    @NamedQuery(name = "UMTPayLegal.getPayLegalByChecknumber",
            query = "SELECT doc from UmtPayLegal doc where doc.checkNumber = :checkNumber"),
})
@Cacheable(false)
public class UmtPayLegal extends DboRootDocument implements Serializable {
  public static final String GENERATOR_PAY_LEGAL = "UMT_PAY_LEGAL_GEN";
  @SuppressWarnings("UnusedDeclaration")
  @TableGenerator(
      name = "umtPayLegalGen",
      table = SEQUENCE_GENERATOR_TABLE_NAME,
      pkColumnName = SEQUENCE_GENERATOR_COLUMN_PK,
      valueColumnName = SEQUENCE_GENERATOR_COLUMN_VALUE,
      pkColumnValue = GENERATOR_PAY_LEGAL)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "umtPayLegalGen")
  private Integer ID;

  @SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "PROVIDER")
  private UMTWebProvider provider;

  @Column(name = "providerCategories")
  private String providerCategories;
  @Column(name = "providerName")
  private String providerName;
  @Column(name = "amount")
  private Float amount;
  @Column(name = "commission")
  private Float commission;
  @Column(name = "receiverTransferSys")
  private Integer receiverTransferSys;
  @Column(name = "checkNumber")
  private String checkNumber;

  @Column(name = "senderBirthPlace", length = 50)
  private String senderBirthPlace;
  @Temporal(TemporalType.DATE)
  @Column(name = "senderBirthday")
  private Date senderBirthday;
  @Column(name = "senderCity", length = 150)
  @Size(max = 150)
  private String senderCity;
  @ManyToOne
  @JoinColumn(name = "SENDERCOUNTRYISO", referencedColumnName = "CODE")
  private Country senderCountryISO;
  @Column(name = "senderFirstName", length = 50)
  private String senderFirstName;
  @Column(name = "senderFlat", length = 5)
  private String senderFlat;
  @Column(name = "senderHouse", length = 5)
  private String senderHouse;
  @Column(name = "senderINN", length = 12)
  private String senderINN;
  @Column(name = "senderIsResident")
  private Integer senderIsResident;
  @Column(name = "senderLastName", length = 50)
  private String senderLastName;
  @Temporal(TemporalType.DATE)
  @Column(name = "senderLicenceDateIssue")
  private Date senderLicenceDateIssue;
  @Temporal(TemporalType.DATE)
  @Column(name = "senderLicenceExpireDate")
  private Date senderLicenceExpireDate;
  @Size(max = 7)
  @Column(name = "senderLicenceDepCode", length = 7)
  private String senderLicenceDepCode;
  @Column(name = "senderLicenceIssuer", length = 150)
  @Size(max = 150)
  private String senderLicenceIssuer;
  @Size(max = 10)
  @Column(name = "senderLicenceNumber", length = 10)
  private String senderLicenceNumber;
  @Size(max = 10)
  @Column(name = "senderLicenceSeries", length = 10)
  private String senderLicenceSeries;
  @Size(max = 120)
  @Column(name = "senderLicenceType", length = 120)
  private String senderLicenceType;
  @ManyToOne
  @JoinColumn(name = "SenderNationality_Code", referencedColumnName = "CODE")
  private Country senderNationalityCode;
  @Temporal(TemporalType.DATE)
  private Date senderOtherDocDateFinish;
  @Temporal(TemporalType.DATE)
  private Date senderOtherDocDateStart;
  @Size(max = 20)
  @Column(name = "senderOtherDocNum", length = 20)
  private String senderOtherDocNum;
  private String senderOtherDocType;
  @Column(name = "senderPatronymic", length = 50)
  private String senderPatronymic;
  @Column(name = "senderPhone", length = 20)
  private String senderPhone;
  @Column(name = "senderEmail", length = 50)
  private String senderEmail;
  @Column(name = "senderStreet", length = 400)
  @Size(max = 400)
  private String senderStreet;
  @Column(name = "senderCardNumber")
  @Size(max = 16)
  private String senderCardNumber;
  @Column(name = "SENDERPOSTCODE")
  @Size(max = 10)
  private String senderPostCode;
  @Column(name = "SenderMigrationCardNumber")
  @Size(max = 30)
  private String senderMigrationCardNumber;
  @Column(name = "SenderHouseBlock", length = 20)
  @Size(max = 20)
  private String senderHouseBlock;
  @Column(name = "SenderHouseBuilding", length = 20)
  @Size(max = 20)
  private String senderHouseBuilding;
  private Integer senderTransferSys;
  @Column(name = "UserSender")
  private int userSender;
  @Column(name = "DocumentNumber", length = 25)
  @Size(max = 25)
  private String documentNumber;
  @Column(name = "TransferIssueCountryCode")
  private String transferIssueCountryCode;
  @Column(name = "PointId")
  Integer pointId;
  @Column(name = "LocationName")
  String locationName;
  @ManyToOne
  @JoinColumn(name = "LocationCountry")
  Country locationCountry;
  @Column(name = "LocationState")
  String locationState;
  @Column(name = "LocationCity")
  String locationCity;
  @Column(name = "LocationStreet")
  String locationStreet;
  @Column(name = "LocationHome")
  String locationHome;
  @Column(name = "ContactPhone")
  String contactPhone;
  @Column(name = "ContactName")
  String contactName;
  @Column(name = "CurrCodeISO")
  @Size(max = 3)
  private String currCodeIso;
  @Basic(fetch = LAZY)
  @Column(name = "STEPSLEFT")
  private Integer stepsLeft;
  @Column(name = "STEPSCOUNT")
  private Integer stepsCount;
  @Column(name = "ProviderCommission")
  private Float providerCommission;
  @Column(name = "AddCommission")
  private Float addCommission;
  @Column(name = "AGENTCOMMISSION")
  private Float agentCommission;
  @Column(name = "SYSCOMMISSION")
  private Float sysCommission;
  @Column(name = "NOTE", length = 50)
  @Size(max = 50)
  private String note;
  @Column(name = "SENDERACCOUNT", length = 34)
  @Size(max = 34)
  private String senderAccount;
  @Temporal(TIMESTAMP)
  @Column(name = "VALUEDATETIME")
  private Date valueDateTime;

  @Lob
  @Basic(fetch = LAZY)
  @Column(name = "EXFIELDS")
  private byte[] exFields;
  @Column(name = "COMMISSIONID")
  private Integer commissionId;
  @Column(name = "DEMOPAY")
  private Integer demoPay;
  @Column(name = "ISREMOTEOPERATIONCOMPLETE")
  private Integer isRemoteOperationComplete;

  @Column(name = "SenderId")
  private Integer senderId;

  public UmtPayLegal() {
    super();
  }

  public String toString() {
    return "com.bssys.entity.PayLegal.UmtPayLegal[ recordPK=" + docPK + " ]";
  }

  public void setProvider(UMTWebProvider provider) {
    this.provider = provider;
    this.providerName = provider.getName();
  }

  public String getProviderName() {
    return providerName;
  }

  public String getProviderCategories() {
    return providerCategories;
  }

  public Float getAmount() {
    return amount;
  }

  public void setAmount(Float amount) {
    this.amount = amount;
  }

  public Float getCommission() {
    return commission;
  }

  public void setReceiverTransferSys(Integer receiverTransferSys) {
    this.receiverTransferSys = receiverTransferSys;
  }

  public String getCheckNumber() {
    return checkNumber;
  }

  public void setCheckNumber(String checkNumber) {
    this.checkNumber = checkNumber;
  }

  public Integer getID() {
    return ID;
  }

  public UMTWebProvider getProvider() {
    return provider;
  }

  public String getSenderBirthPlace() {
    return senderBirthPlace;
  }

  public void setSenderBirthPlace(String senderBirthPlace) {
    this.senderBirthPlace = senderBirthPlace;
  }

  public Date getSenderBirthday() {
    return senderBirthday;
  }

  public void setSenderBirthday(Date senderBirthday) {
    this.senderBirthday = senderBirthday;
  }

  public String getSenderCity() {
    return senderCity;
  }

  public void setSenderCity(String senderCity) {
    this.senderCity = senderCity;
  }

  public Country getSenderCountryISO() {
    return senderCountryISO;
  }

  public void setSenderCountryISO(Country senderCountryISO) {
    this.senderCountryISO = senderCountryISO;
  }

  public String getSenderFirstName() {
    return senderFirstName;
  }

  public void setSenderFirstName(String senderFirstName) {
    this.senderFirstName = senderFirstName;
  }

  public String getSenderFlat() {
    return senderFlat;
  }

  public void setSenderFlat(String senderFlat) {
    this.senderFlat = senderFlat;
  }

  public String getSenderHouse() {
    return senderHouse;
  }

  public void setSenderHouse(String senderHouse) {
    this.senderHouse = senderHouse;
  }

  public String getSenderINN() {
    return senderINN;
  }

  public void setSenderINN(String senderINN) {
    this.senderINN = senderINN;
  }

  public Integer getSenderIsResident() {
    return senderIsResident;
  }

  public void setSenderIsResident(Integer senderIsResident) {
    this.senderIsResident = senderIsResident;
  }

  public String getSenderLastName() {
    return senderLastName;
  }

  public void setSenderLastName(String senderLastName) {
    this.senderLastName = senderLastName;
  }

  public Date getSenderLicenceDateIssue() {
    return senderLicenceDateIssue;
  }

  public void setSenderLicenceDateIssue(Date senderLicenceDateIssue) {
    this.senderLicenceDateIssue = senderLicenceDateIssue;
  }

  public String getSenderLicenceDepCode() {
    return senderLicenceDepCode;
  }

  public void setSenderLicenceDepCode(String senderLicenceDepCode) {
    this.senderLicenceDepCode = senderLicenceDepCode;
  }

  public String getSenderLicenceIssuer() {
    return senderLicenceIssuer;
  }

  public void setSenderLicenceIssuer(String senderLicenceIssuer) {
    this.senderLicenceIssuer = senderLicenceIssuer;
  }

  public String getSenderLicenceNumber() {
    return senderLicenceNumber;
  }

  public void setSenderLicenceNumber(String senderLicenceNumber) {
    this.senderLicenceNumber = senderLicenceNumber;
  }

  public String getSenderLicenceSeries() {
    return senderLicenceSeries;
  }

  public void setSenderLicenceSeries(String senderLicenceSeries) {
    this.senderLicenceSeries = senderLicenceSeries;
  }

  public String getSenderLicenceType() {
    return senderLicenceType;
  }

  public void setSenderLicenceType(String senderLicenceType) {
    this.senderLicenceType = senderLicenceType;
  }

  public Country getSenderNationalityCode() {
    return senderNationalityCode;
  }

  public void setSenderNationalityCode(Country senderNationality_Code) {
    this.senderNationalityCode = senderNationality_Code;
  }

  public Date getSenderOtherDocDateFinish() {
    return senderOtherDocDateFinish;
  }

  public void setSenderOtherDocDateFinish(Date senderOtherDocDateFinish) {
    this.senderOtherDocDateFinish = senderOtherDocDateFinish;
  }

  public Date getSenderOtherDocDateStart() {
    return senderOtherDocDateStart;
  }

  public void setSenderOtherDocDateStart(Date senderOtherDocDateStart) {
    this.senderOtherDocDateStart = senderOtherDocDateStart;
  }

  public String getSenderOtherDocNum() {
    return senderOtherDocNum;
  }

  public void setSenderOtherDocNum(String senderOtherDocNum) {
    this.senderOtherDocNum = senderOtherDocNum;
  }

  public String getSenderOtherDocType() {
    return senderOtherDocType;
  }

  public void setSenderOtherDocType(String senderOtherDocType) {
    this.senderOtherDocType = senderOtherDocType;
  }

  public String getSenderPatronymic() {
    return senderPatronymic;
  }

  public void setSenderPatronymic(String senderPatronymic) {
    this.senderPatronymic = senderPatronymic;
  }

  public String getSenderPhone() {
    return senderPhone;
  }

  public void setSenderPhone(String senderPhone) {
    this.senderPhone = senderPhone;
  }

  public String getSenderStreet() {
    return senderStreet;
  }

  public void setSenderStreet(String senderStreet) {
    this.senderStreet = senderStreet;
  }

  public void setSenderTransferSys(Integer senderTransferSys) {
    this.senderTransferSys = senderTransferSys;
  }

  public String getSenderEmail() {
    return senderEmail;
  }

  public void setSenderEmail(String senderEmail) {
    this.senderEmail = senderEmail;
  }

  public String getSenderCardNumber() {
    return senderCardNumber;
  }

  public void setSenderCardNumber(String senderCardNumber) {
    this.senderCardNumber = senderCardNumber;
  }


  public void setUserSender(int userSender) {
    this.userSender = userSender;
  }

  public void setDocumentNumber(String documentNumber) {
    assert (documentNumber != null);
    this.documentNumber = documentNumber;
  }

  public String getDocumentNumber() {
    return documentNumber;
  }

  public String getTransferIssueCountryCode() {
    return transferIssueCountryCode;
  }

  public void setTransferIssueCountryCode(String transferIssueCountryCode) {
    this.transferIssueCountryCode = transferIssueCountryCode;
  }

  public void setPointId(Integer pointId) {
    this.pointId = pointId;
  }

  public void setLocationName(String locationName) {
    this.locationName = locationName;
  }

  public void setLocationCountry(Country locationCountry) {
    this.locationCountry = locationCountry;
  }

  public void setLocationState(String locationState) {
    this.locationState = locationState;
  }

  public void setLocationCity(String locationCity) {
    this.locationCity = locationCity;
  }

  public void setLocationStreet(String locationStreet) {
    this.locationStreet = locationStreet;
  }

  public void setLocationHome(String locationHome) {
    this.locationHome = locationHome;
  }

  public void setContactPhone(String contactPhone) {
    this.contactPhone = contactPhone;
  }

  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  public void setProviderCategories(String providerCategories) {
    this.providerCategories = providerCategories;
  }

  public String getCurrCodeIso() {
    return currCodeIso;
  }

  public void setCurrCodeIso(String currcodeiso) {
    this.currCodeIso = currcodeiso;
  }

  public String getAmountWithCurrency() {
    return new Money(amount, currCodeIso).toString();
  }

  public Integer getStepsLeft() {
    return stepsLeft;
  }

  public void setStepsLeft(Integer stepsLeft) {
    this.stepsLeft = stepsLeft;
  }

  public void clearID() {
    this.ID = null;
  }

  public Integer getStepsCount() {
    return stepsCount;
  }

  public void setStepsCount(Integer stepsCount) {
    this.stepsCount = stepsCount;
  }

  public int getCurrentStep() {
    if (stepsCount == null || stepsLeft == null) {
      return 0;
    }
    return stepsCount - stepsLeft;
  }

  public Float getAddCommission() {
    return addCommission;
  }

  public void setAddCommission(Float addCommission) {
    this.addCommission = addCommission;
  }

  public Float getProviderCommission() {
    return providerCommission;
  }

  public void setProviderCommission(Float providerCommission) {
    this.providerCommission = providerCommission;
  }

  public void setCommission(Float commission) {
    this.commission = commission;
  }

  public Float getAgentCommission() {
    return agentCommission;
  }

  public void setAgentCommission(Float agentCommission) {
    this.agentCommission = agentCommission;
  }

  public Float getSysCommission() {
    return sysCommission;
  }

  public void setSysCommission(Float sysCommission) {
    this.sysCommission = sysCommission;
  }

  public void setExFields(byte[] exFields) {
    this.exFields = exFields;
  }

  public String getSenderPostCode() {
    return senderPostCode;
  }

  public void setSenderPostCode(String senderPostCode) {
    this.senderPostCode = senderPostCode;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public String getSenderAccount() {
    return senderAccount;
  }

  public void setSenderAccount(String senderAccount) {
    this.senderAccount = senderAccount;
  }

  public Date getValueDateTime() {
    return valueDateTime;
  }

  public void setValueDateTime(Date valueDateTime) {
    this.valueDateTime = valueDateTime;
  }

  public void setCommissionId(Integer commissionId) {
    this.commissionId = commissionId;
  }

  public Integer getCommissionId() {
    return commissionId;
  }

  public void setDemoPay(Integer demoPay) {
    this.demoPay = demoPay;
  }

  public Integer getDemoPay() {
    return demoPay;
  }

  public Integer getIsRemoteOperationComplete() {
    return isRemoteOperationComplete;
  }

  public void setIsRemoteOperationComplete(Integer isRemoteOperationComplete) {
    this.isRemoteOperationComplete = isRemoteOperationComplete;
  }

  public byte[] getExFields() {
    return exFields;
  }

  public String getSenderMigrationCardNumber() {
    return senderMigrationCardNumber;
  }

  public void setSenderMigrationCardNumber(String senderMigrationCardNumber) {
    this.senderMigrationCardNumber = senderMigrationCardNumber;
  }

  public String getSenderHouseBlock() {
    return senderHouseBlock;
  }

  public void setSenderHouseBlock(String senderHouseBlock) {
    this.senderHouseBlock = senderHouseBlock;
  }

  public String getSenderHouseBuilding() {
    return senderHouseBuilding;
  }

  public void setSenderHouseBuilding(String senderHouseBuilding) {
    this.senderHouseBuilding = senderHouseBuilding;
  }

  public Date getSenderLicenceExpireDate() {
    return senderLicenceExpireDate;
  }

  public void setSenderLicenceExpireDate(Date senderLicenceExpireDate) {
    this.senderLicenceExpireDate = senderLicenceExpireDate;
  }

  public Integer getSenderId() {
    return senderId;
  }

  public void setSenderId(Integer senderId) {
    this.senderId = senderId;
  }
}
