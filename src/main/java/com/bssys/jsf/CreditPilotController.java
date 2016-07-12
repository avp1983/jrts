package com.bssys.jsf;

import com.bssys.ejb.creditpilot.CreditPilotDictionaryBean;
import com.bssys.ejb.creditpilot.CreditPilotDocBean;
import com.bssys.ejb.creditpilot.CreditPilotFacade;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;

@Named("creditPilotController")
@ApplicationScoped
public class CreditPilotController implements Serializable {

  @Inject
  CreditPilotFacade creditPilotFacade;
  @Inject
  CreditPilotDocBean creditPilotDocBean;
  @Inject
  CreditPilotDictionaryBean creditPilotDictionaryBean;

  private void writeResponse(int httpCode, String outResult) {
    try {
      FacesContext fc = FacesContext.getCurrentInstance();
      ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
      ec.setResponseContentType("text/plain");
      ec.setResponseCharacterEncoding("UTF-8");
      ec.setResponseStatus(httpCode);
      ec.getResponseOutputWriter().write(outResult);
      fc.responseComplete();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String exceptionToStr(Throwable t) {
    try (StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter(sw)) {
      t.printStackTrace(pw);
      return sw.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }

  public void loadProviders() {
    int httpCode = SC_OK;
    String outResult;

    try {
      outResult = creditPilotDictionaryBean.loadProviders();
    } catch (Exception e) {
      httpCode = SC_BAD_REQUEST;
      outResult = exceptionToStr(e);
    }

    writeResponse(httpCode, outResult);
  }

  public void loadCategories() {
    int httpCode = SC_OK;
    String outResult;

    try {
      outResult = creditPilotDictionaryBean.loadCategories();
    } catch (Exception e) {
      httpCode = SC_BAD_REQUEST;
      outResult = exceptionToStr(e);
    }

    writeResponse(httpCode, outResult);
  }

  public void loadMessages() {
    int httpCode = SC_OK;
    String outResult;

    try {
      outResult = creditPilotDictionaryBean.loadMessages();
    } catch (Exception e) {
      httpCode = SC_BAD_REQUEST;
      outResult = exceptionToStr(e);
    }

    writeResponse(httpCode, outResult);
  }

  public void loadBalance() {
    int httpCode = SC_OK;
    String outResult;

    try {
      outResult = creditPilotDictionaryBean.loadBalance();
    } catch (Exception e) {
      httpCode = SC_BAD_REQUEST;
      outResult = exceptionToStr(e);
    }

    writeResponse(httpCode, outResult);
  }

  public void checkNotFinishedDocs() {
    int httpCode = SC_OK;

    try {
      creditPilotDocBean.checkNotFinishedDocs();
    } catch (Exception e) {
      httpCode = SC_BAD_REQUEST;
    }

    writeResponse(httpCode, "");
  }
}
