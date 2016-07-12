package com.bssys.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@NamedQueries({
    @NamedQuery(name = "UmtTransferClient.cardNumberStartWith", query = "SELECT Cl FROM UmtTransferClient Cl WHERE " +
        "Cl.cardNumber like :query "),
  @NamedQuery(name = "UmtTransferClient.getByCardNumber", query = "SELECT Cl FROM UmtTransferClient Cl WHERE " +
  "Cl.cardNumber = :query ")

})
@Cacheable(value = false)
public class UmtTransferClient implements Serializable {

  @Id
  @Column(name = "Id")
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "account", length = 34)
  @Size(max = 34)
  private String account;
  @Column(name = "birthPlace", length = 50)
  @Size(max = 50)
  private String birthPlace;
  @Temporal(TemporalType.DATE)
  @Column(name = "birthday")
  private Date birthday;
  @Column(name = "city", length = 150)
  @Size(max = 150)
  private String city;
  @ManyToOne
  @JoinColumn(name = "COUNTRYISO", referencedColumnName = "CODE")
  private Country countryISO;
  @Column(name = "firstName", length = 50)
  @Size(max = 50)
  private String firstName;
  @Column(name = "flat", length = 5)
  @Size(max = 5)
  private String flat;
  @Column(name = "house", length = 5)
  @Size(max = 5)
  private String house;
  @Column(name = "iNN", length = 12)
  @Size(max = 12)
  private String iNN;
  @Column(name = "isResident")
  private Integer isResident;
  @Column(name = "lastName", length = 50)
  @Size(max = 50)
  private String lastName;
  @Temporal(TemporalType.DATE)
  @Column(name = "licenceDateIssue")
  private Date licenceDateIssue;
  @Column(name = "licenceDepCode", length = 7)
  @Size(max = 7)
  private String licenceDepCode;
  @Column(name = "licenceIssuer", length = 150)
  @Size(max = 150)
  private String licenceIssuer;
  @Column(name = "licenceNumber", length = 20)
  @Size(max = 20)
  private String licenceNumber;
  @Column(name = "licenceSeries", length = 10)
  @Size(max = 10)
  private String licenceSeries;
  @Column(name = "licenceType", length = 120)
  @Size(max = 120)
  private String licenceType;
  @Column(name = "migrationCardNumber", length = 30)
  @Size(max = 30)
  private String migrationCardNumber;
  @ManyToOne
  @JoinColumn(name = "NATIONALITY_CODE", referencedColumnName = "CODE")
  private Country nationalityCode;
  @Column(name = "patronymic", length = 50)
  @Size(max = 50)
  private String patronymic;
  @Column(name = "phone", length = 20)
  @Size(max = 20)
  private String phone;
  @Column(name = "email", length = 50)
  @Size(max = 50)
  private String email;
  @Column(name = "street", length = 400)
  @Size(max= 400)
  private String street;
  @Column(name = "Id_User", length = 16)
  @Size(max = 16)
  private String cardNumber;

  @Column(name = "HouseBlock", length = 20)
  @Size(max = 20)
  private String houseBlock;
  @Column(name = "HouseBuilding", length = 20)
  @Size(max = 20)
  private String houseBuilding;

  @Column(name = "CLIENT")
  private Integer client;
  @Column(name = "CUSTID")
  private Integer custId;
  @Column(name = "systemid")
  private Integer systemId;



  public Integer getId() {
    return id;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getBirthPlace() {
    return birthPlace;
  }

  public void setBirthPlace(String birthPlace) {
    this.birthPlace = birthPlace;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public Country getCountryISO() {
    return countryISO;
  }

  public void setCountryISO(Country countryISO) {
    this.countryISO = countryISO;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getFlat() {
    return flat;
  }

  public void setFlat(String flat) {
    this.flat = flat;
  }

  public String getHouse() {
    return house;
  }

  public void setHouse(String house) {
    this.house = house;
  }

  public String getINN() {
    return iNN;
  }

  public void setiNN(String iNN) {
    this.iNN = iNN;
  }

  public Integer getIsResident() {
    return isResident;
  }

  public void setIsResident(Integer isResident) {
    this.isResident = isResident;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Date getLicenceDateIssue() {
    return licenceDateIssue;
  }

  public void setLicenceDateIssue(Date licenceDateIssue) {
    this.licenceDateIssue = licenceDateIssue;
  }

  public String getLicenceDepCode() {
    return licenceDepCode;
  }

  public void setLicenceDepCode(String licenceDepCode) {
    this.licenceDepCode = licenceDepCode;
  }

  public String getLicenceIssuer() {
    return licenceIssuer;
  }

  public void setLicenceIssuer(String licenceIssuer) {
    this.licenceIssuer = licenceIssuer;
  }

  public String getLicenceNumber() {
    return licenceNumber;
  }

  public void setLicenceNumber(String licenceNumber) {
    this.licenceNumber = licenceNumber;
  }

  public String getLicenceSeries() {
    return licenceSeries;
  }

  public void setLicenceSeries(String licenceSeries) {
    this.licenceSeries = licenceSeries;
  }

  public String getLicenceType() {
    return licenceType;
  }

  public void setLicenceType(String licenceType) {
    this.licenceType = licenceType;
  }

  public String getMigrationCardNumber() {
    return migrationCardNumber;
  }

  public void setMigrationCardNumber(String migrationCardNumber) {
    this.migrationCardNumber = migrationCardNumber;
  }

  public Country getNationalityCode() {
    return nationalityCode;
  }

  public void setNationalityCode(Country nationalityCode) {
    this.nationalityCode = nationalityCode;
  }

  public String getPatronymic() {
    return patronymic;
  }

  public void setPatronymic(String patronymic) {
    this.patronymic = patronymic;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  public String getHouseBlock() {
    return houseBlock;
  }

  public void setHouseBlock(String houseBlock) {
    this.houseBlock = houseBlock;
  }

  public String getHouseBuilding() {
    return houseBuilding;
  }

  public void setHouseBuilding(String houseBuilding) {
    this.houseBuilding = houseBuilding;
  }

  public Integer getClient() {
    return client;
  }

  public void setClient(Integer client) {
    this.client = client;
  }

  public Integer getCustId() {
    return custId;
  }

  public void setCustId(Integer custId) {
    this.custId = custId;
  }

  public Integer getSystemId() {
    return systemId;
  }

  public void setSystemId(Integer systemId) {
    this.systemId = systemId;
  }
}