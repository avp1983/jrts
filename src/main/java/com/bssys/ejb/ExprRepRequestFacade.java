package com.bssys.ejb;

import com.bssys.api.types.Document;
import com.bssys.api.types.IdType;
import com.bssys.api.types.ObjectFactory;
import com.bssys.bls.types.BlsDateTime;
import com.bssys.bls.types.DboDocPK;
import com.bssys.bls.types.DboRootDocumentFacade;
import com.bssys.entity.ExprRepRequest;
import com.bssys.umt.Statuses;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Named("ExprRepRequestFacade")
@Stateless
public class ExprRepRequestFacade implements Serializable {
  private ObjectFactory objectFactory = new ObjectFactory();
  private static final int REPORT_ID_CLIENT_TRANSFER = 120; /*см EXPRICREPORTS*/


  @Inject
  private DboRootDocumentFacade rootDocumentFacade;
  @Inject
  private TransferClientFacade clientFacade;


  @PersistenceContext
  private transient EntityManager em;

  public Integer getCountUserReportsOfType(int userKey, int reportId) {
    Query query = em.createNativeQuery("select cast(count(*) as DECIMAL )" +
            "from ExprRepRequest " +
            "where Client = ? and ReportId = ?")
            .setParameter(1, userKey)
            .setParameter(2, reportId);
    return ((BigDecimal) query.getSingleResult()).intValue();
  }

  public ExprRepRequest createNewDocument(int client) {
    ExprRepRequest doc = new ExprRepRequest();
    rootDocumentFacade.prepareDocumentForSend(doc, client);
    em.persist(doc);
    em.flush();
    return doc;
  }

  public ExprRepRequest getByPK(DboDocPK dboDocPK) {
    TypedQuery<ExprRepRequest> query = em.createNamedQuery("ExprRepRequest.getByPK", ExprRepRequest.class);
    query.setParameter("docPK", dboDocPK);
    try{
      return query.getSingleResult();}
    catch(NoResultException e){
      return null;
    }
  }

  private byte[] getReportParams(Document.Client.TransferRequest transferRequest, Integer clientId){
    String params = "<?xml version=\"1.0\" encoding=\"windows-1251\"?><REPADDINFO><PARAMS><ADDPARAMS>" +
      "<TransferType>"+ transferRequest.getTransferType() +"</TransferType>"+
      "<ClientId>"+ clientId.toString() +"</ClientId>"+
      "</ADDPARAMS><FORMATS>3</FORMATS></PARAMS></REPADDINFO>";

    return params.getBytes();
  }

  public IdType createRequestByApiRequest(Document request, Integer clientId){
    ExprRepRequest exprRepRequest = createNewDocument(request.getMemberId().intValue());
    exprRepRequest.setReportTypeId(REPORT_ID_CLIENT_TRANSFER);
    exprRepRequest.setReportId(REPORT_ID_CLIENT_TRANSFER);
    exprRepRequest.setReportparams(getReportParams(request.getClient().getTransferRequest(),clientId));
    exprRepRequest.setStatus(Statuses.STS_C_IN_QUEUE);

    em.merge(exprRepRequest);
    em.flush();
    IdType idType = objectFactory.createIdType();
    idType.setDateCreate(BigInteger.valueOf(exprRepRequest.getDocPK().getDatecreate()));
    idType.setTimeCreate(BigInteger.valueOf(exprRepRequest.getDocPK().getTimecreate()));
    return idType;
  }

  public void deleteDocument(ExprRepRequest exprRepRequest){
    em.remove(exprRepRequest);
  }
}
