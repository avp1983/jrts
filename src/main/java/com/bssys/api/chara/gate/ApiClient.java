package com.bssys.api.chara.gate;

import com.bssys.api.types.*;
import com.bssys.bls.types.DboDocPK;
import com.bssys.ejb.ExprRepRequestFacade;
import com.bssys.ejb.TransferClientFacade;
import com.bssys.entity.ExprRepRequest;
import com.bssys.entity.UmtTransferClient;
import com.google.common.base.Strings;
import org.joda.time.DateTime;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.List;
import com.bssys.api.types.ObjectFactory;
import org.xml.sax.InputSource;
import static com.bssys.umt.Statuses.*;

/**
 * Created by e.zemtcovsky on 24.02.2015.
 */

@Path("client")
@Named
@Stateless
public class ApiClient {
  @Inject
  private TransferClientFacade clientFacade;
  @Inject
  private ExprRepRequestFacade exprRepRequestFacade;

  private ObjectFactory objectFactory = new ObjectFactory();

  @POST
  @Path("search-request")
  @Consumes(MediaType.APPLICATION_XML)
  @Produces(MediaType.APPLICATION_XML)
  public Document sendClientSearchReply(Document request) {
    try {
    Document.Client.SearchRequest searchRequest = request.getClient().getSearchRequest();

    if (isEmptySearchRequest(searchRequest)){
      throw new ApiException(ApiExceptionType.API_ERR_CLIENT_SEARCH_REQUEST_EMPTY, "Для поиска клиента заполните хотя бы один атрибут в запросе");
    }

    Document.Client clientReply = objectFactory.createDocumentClient();
    List<ClientType> clientSearchReply = clientReply.getSearchReply();

    List<UmtTransferClient> clientList = clientFacade.getByAPIRequest(searchRequest);
    if (clientList.isEmpty()){
      throw new ApiException(ApiExceptionType.API_ERR_CLIENT_NOT_FOUND, "Не найден ни один клиент, удовлетворяющий запросу");
    }
    for (UmtTransferClient client : clientList) {
      clientSearchReply.add(getClientReply(client));
    }

    Document document = ApiDocumentConverter.getDocumentWithHeaders(request);
    document.setClient(clientReply);
    return document;


    } catch (ApiException e) {
      Document document = ApiDocumentConverter.getDocumentWithHeaders(request);
      document.setError(e.getError());
      return document;
    }
  }

  private ClientType getClientReply(UmtTransferClient umtTransferClient){
    ClientType clientType = objectFactory.createClientType();
    ClientType.Data data = objectFactory.createClientTypeData();
    data.setLastname(umtTransferClient.getLastName());
    data.setFirstname(umtTransferClient.getFirstName());
    data.setPatronymic(umtTransferClient.getPatronymic());
    data.setAccount(umtTransferClient.getAccount());
    data.setIsresident(umtTransferClient.getIsResident());
    data.setIdCard(umtTransferClient.getCardNumber());
    data.setIdClient(BigInteger.valueOf(umtTransferClient.getId()));
    if (umtTransferClient.getNationalityCode() != null) {
      CountryType country = objectFactory.createCountryType();
      country.setNumeric(umtTransferClient.getNationalityCode().getCode());
      data.setCountry(country);
    }
    clientType.setData(data);

    ClientType.Address address = objectFactory.createClientTypeAddress();
    address.setCity(umtTransferClient.getCity());
    address.setStreet(umtTransferClient.getStreet());
    address.setHouse(umtTransferClient.getHouse());
    address.setHouseBlock(umtTransferClient.getHouseBlock());
    address.setHouseBuilding(umtTransferClient.getHouseBuilding());
    address.setFlat(umtTransferClient.getFlat());
    address.setPhone(umtTransferClient.getPhone());
    address.setEmail(umtTransferClient.getEmail());
    if (umtTransferClient.getCountryISO() != null) {
      CountryType country = objectFactory.createCountryType();
      country.setNumeric(umtTransferClient.getCountryISO().getCode());
      address.setCountry(country);
    }
    clientType.setAddress(address);

    DataExtendedType dataExtendedType = objectFactory.createDataExtendedType();
    dataExtendedType.setInn(umtTransferClient.getINN());
    try {
      if (umtTransferClient.getBirthday() != null) {
        dataExtendedType.setBirthDay(DatatypeFactory.newInstance().newXMLGregorianCalendar(new DateTime(umtTransferClient.getBirthday()).toGregorianCalendar()));
      }
    } catch (DatatypeConfigurationException e) {
      e.printStackTrace();
    }
    dataExtendedType.setBirthPlace(umtTransferClient.getBirthPlace());
    dataExtendedType.setMigrationCardNumber(umtTransferClient.getMigrationCardNumber());
    clientType.setDataExtended(dataExtendedType);

    IdentificationType identificationType = objectFactory.createIdentificationType();
    identificationType.setType(umtTransferClient.getLicenceType());
    identificationType.setSeries(umtTransferClient.getLicenceSeries());
    identificationType.setNumber(umtTransferClient.getLicenceNumber());
    identificationType.setIssuer(umtTransferClient.getLicenceIssuer());
    try {
      if (umtTransferClient.getLicenceDateIssue() != null) {
        identificationType.setIssueDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(new DateTime(umtTransferClient.getLicenceDateIssue()).toGregorianCalendar()));
      }
    } catch (DatatypeConfigurationException e) {
      e.printStackTrace();
    }
    identificationType.setDepCode(umtTransferClient.getLicenceDepCode());

    clientType.setIdentification(identificationType);
    return clientType;
  }

  @GET
  @Path("ping")
  @Produces(MediaType.TEXT_PLAIN)
  public String checkAvailable() {
    return "ok.";
  }

  private boolean isEmptySearchRequest(Document.Client.SearchRequest searchRequest){
    return Strings.isNullOrEmpty(searchRequest.getSeries()) &&
      Strings.isNullOrEmpty(searchRequest.getNumber()) &&
      Strings.isNullOrEmpty(searchRequest.getInn()) &&
      Strings.isNullOrEmpty(searchRequest.getPhone()) &&
      Strings.isNullOrEmpty(searchRequest.getIdCard()) &&
      searchRequest.getIdClient() == null;
  }

  private boolean isEmptyTransferRequest(Document.Client.TransferRequest transferRequest){
    return Strings.isNullOrEmpty(transferRequest.getIdCard()) &&
      transferRequest.getIdClient() == null;
  }


  @POST
  @Path("transfer-request")
  @Consumes(MediaType.APPLICATION_XML)
  @Produces(MediaType.APPLICATION_XML)
  public Document sendClientTransferReply(Document request){
    try {

      Document.Client.TransferRequest transferRequest = request.getClient().getTransferRequest();

      if (isEmptyTransferRequest(transferRequest)){
        throw new ApiException(ApiExceptionType.API_ERR_CLIENT_TRANSFER_REQUEST_EMPTY, "Для получения переводов клиента заполните идентификатор клиента или номер его карты");
      }

      UmtTransferClient client = getClient(transferRequest);
      if (client == null){
        throw new ApiException(ApiExceptionType.API_ERR_CLIENT_NOT_FOUND, "Не найден ни один клиент, удовлетворяющий запросу");
      }

      IdType idType = exprRepRequestFacade.createRequestByApiRequest(request, client.getId());



      Document.Client clientReply = objectFactory.createDocumentClient();


      clientReply.setTransferReply(idType);
      Document document = ApiDocumentConverter.getDocumentWithHeaders(request);
      document.setClient(clientReply);
      return document;

    } catch (ApiException e) {
      Document document = ApiDocumentConverter.getDocumentWithHeaders(request);
      document.setError(e.getError());
      return document;
    }
  }

  private UmtTransferClient getClient(Document.Client.TransferRequest transferRequest){
    UmtTransferClient client;
    if (transferRequest.getIdClient() != null){
      client = clientFacade.getById(transferRequest.getIdClient().intValue());
    }
    else{
      client = clientFacade.getByCardNumber(transferRequest.getIdCard());
    }
    return client;
  }

  @POST
  @Path("transfer-data-request")
  @Consumes(MediaType.APPLICATION_XML)
  @Produces(MediaType.APPLICATION_XML)
  public Document sendClientTransferDataReply(Document request){
    try {

      IdType transferDataRequest = request.getClient().getTransferDataRequest();
      ExprRepRequest exprRepRequest = exprRepRequestFacade.getByPK(new DboDocPK(request.getMemberId().intValue(),transferDataRequest.getDateCreate().intValue(),transferDataRequest.getTimeCreate().intValue()));
      checkReportRequest(exprRepRequest);
      Document.Client clientReply = null;
      try {
        String decoded = new String(exprRepRequest.getReadyreport(), "windows-1251");
        JAXBContext context = JAXBContext.newInstance(Document.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Document unmarshal = (Document) unmarshaller.unmarshal(new InputSource(new StringReader(decoded)));
        clientReply = unmarshal.getClient();

      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      } catch (JAXBException e) {
        e.printStackTrace();
      }
      Document document = ApiDocumentConverter.getDocumentWithHeaders(request);
      document.setClient(clientReply);
      exprRepRequestFacade.deleteDocument(exprRepRequest);
      return document;
    } catch (ApiException e) {
      Document document = ApiDocumentConverter.getDocumentWithHeaders(request);
      document.setError(e.getError());
      return document;
    }
  }

  private void checkReportRequest(ExprRepRequest exprRepRequest) throws ApiException {
    if (exprRepRequest == null){
      throw new ApiException(ApiExceptionType.API_ERR_TRANSFER_REQUEST_NOT_FOUND, "Отчёт не найден");
    }

    if (exprRepRequest.getStatus() == STS_C_IN_QUEUE ){
      throw new ApiException(ApiExceptionType.API_ERR_TRANSFER_REQUEST_IN_QUEUE, "Отчет в очереди");
    }

    if (exprRepRequest.getStatus() == STS_C_EXECUTING ){
      throw new ApiException(ApiExceptionType.API_ERR_TRANSFER_REQUEST_IN_PROCESS, "Отчёт выполняется");
    }

    if (exprRepRequest.getStatus() == STS_C_ERROR ){
      throw new ApiException(ApiExceptionType.API_ERR_TRANSFER_REQUEST_ERROR, "Ошибка при формирования отчета");
    }

    if (exprRepRequest.getStatus() == STS_C_NODATA ){
      throw new ApiException(ApiExceptionType.API_ERR_TRANSFER_REQUEST_NODATA, "Отчет не содержит данных");
    }
  }

}
