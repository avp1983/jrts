package com.bssys.api.chara.gate;

import com.bssys.api.types.Document;
import com.bssys.api.types.TransferLegalArchiveRequest;
import com.bssys.api.types.TransferLegalSend;
import com.bssys.entity.PayLegal.UmtPayLegal;
import com.bssys.entity.PayLegal.UmtPayLegalExFields;
import com.bssys.session.UserSessionBean;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;


@Path("paylegal")
@Named
@Stateless
public class ApiPayLegal {

  @Inject
  private ApiPayLegalFacade apiPayLegalFacade;

  @Inject
  private ApiPayLegalControls apiPayLegalControls;

  @Inject
  private ApiDocumentConverter apiDocumentConverter;

  @Inject
  private UserSessionBean userSession;

  public ApiPayLegal() {
  }

  @GET
  @Path("ping")
  @Produces(MediaType.TEXT_PLAIN)
  public String checkAvailable() {
    return "ok.";
  }

  @POST
  @Path("create-paylegal")
  @Consumes(MediaType.APPLICATION_XML)
  @Produces(MediaType.APPLICATION_XML)
  public Document createPayLegalTransfer(Document request) {
    try {
      userSession.init(request.getMemberId().intValue());
      apiPayLegalControls.checkPointCreditPilotAllowed(userSession.getAgentId());
      apiPayLegalControls.checkCurrCode(request.getTransferLegal().getCreateRequest());

      TransferLegalSend legalCreate = request.getTransferLegal().getCreateRequest();
      UmtPayLegalExFields exFields = apiDocumentConverter.parseExFields(legalCreate);
      UmtPayLegal payLegal = apiPayLegalFacade.createPayLegal(legalCreate, exFields);
      apiPayLegalControls.checkPointOwnerOfPayLegal(request.getMemberId().intValue(), payLegal);

      apiDocumentConverter.parseUmtPayLegal(legalCreate, payLegal);
      apiPayLegalFacade.savePayLegal(payLegal, exFields);

      return apiDocumentConverter.parseResponsePayLegal(request, payLegal, exFields);
    } catch (ApiException e) {
      Document document = ApiDocumentConverter.getDocumentWithHeaders(request);
      document.setError(e.getError());
      return document;
    }
  }

  @POST
  @Path("send-paylegal")
  @Consumes(MediaType.APPLICATION_XML)
  @Produces(MediaType.APPLICATION_XML)
  public Document sendPayLegalTransfer(Document request) {
    try {
      userSession.init(request.getMemberId().intValue());
      apiPayLegalControls.checkPointCreditPilotAllowed(userSession.getAgentId());
      apiPayLegalControls.checkCurrCode(request.getTransferLegal().getSendRequest());

      TransferLegalSend legalSend = request.getTransferLegal().getSendRequest();
      UmtPayLegalExFields exFields = apiDocumentConverter.parseExFields(legalSend);
      UmtPayLegal payLegal = apiPayLegalFacade.getPayLegal(legalSend, exFields);
      apiPayLegalControls.checkPointOwnerOfPayLegal(request.getMemberId().intValue(), payLegal);

      apiDocumentConverter.parseUmtPayLegal(legalSend, payLegal);
      apiPayLegalFacade.confirmPayLegal(payLegal);

      return apiDocumentConverter.parseResponseSendPayLegal(request, payLegal);
    } catch (ApiException e) {
      Document document = ApiDocumentConverter.getDocumentWithHeaders(request);
      document.setError(e.getError());
      return document;
    }
  }

  @POST
  @Path("delete-paylegal")
  @Consumes(MediaType.APPLICATION_XML)
  @Produces(MediaType.APPLICATION_XML)
  public Document deletePayLegalTransfer(Document request) {
    try {
      userSession.init(request.getMemberId().intValue());
      apiPayLegalControls.checkPointCreditPilotAllowed(userSession.getAgentId());

      Document.TransferLegal.DeleteRequest legalDelete = request.getTransferLegal().getDeleteRequest();
      UmtPayLegal payLegal = apiPayLegalFacade.getPayLegal(legalDelete);
      apiPayLegalControls.checkPointOwnerOfPayLegal(request.getMemberId().intValue(), payLegal);

      apiPayLegalFacade.deletePayLegal(payLegal);
      return apiDocumentConverter.parseResponseDeletePayLegal(request, payLegal);
    } catch (ApiException e) {
      Document document = ApiDocumentConverter.getDocumentWithHeaders(request);
      document.setError(e.getError());
      return document;
    }
  }

  @POST
  @Path("archive-paylegal")
  @Consumes(MediaType.APPLICATION_XML)
  @Produces(MediaType.APPLICATION_XML)
  public Document archivePayLegalTransfer(Document request) {
    try {
      userSession.init(request.getMemberId().intValue());
      apiPayLegalControls.checkPointCreditPilotAllowed(userSession.getAgentId());

      TransferLegalArchiveRequest archiveRequest = request.getTransferLegal().getArchiveRequest();
      List<UmtPayLegal> payLegalList = apiPayLegalFacade.getArchivePayLegal(archiveRequest);
      return apiDocumentConverter.parseResponseArchivePayLegal(request, payLegalList);
    } catch (ApiException e) {
      Document document = ApiDocumentConverter.getDocumentWithHeaders(request);
      document.setError(e.getError());
      return document;
    }
  }
}
