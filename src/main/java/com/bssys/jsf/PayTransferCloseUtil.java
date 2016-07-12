package com.bssys.jsf;

import com.bssys.bls.types.DboDocPK;
import com.bssys.ejb.UmtPayTransferCloseFacade;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named("payTransferCloseUtil")
@ApplicationScoped
public class PayTransferCloseUtil implements Serializable {

  @Inject
  private UmtPayTransferCloseFacade umtPayTransferCloseFacade;

  public String getDocumentNumberByIDR(String idr) {
    return umtPayTransferCloseFacade.getDocumentNumberByIDR(new DboDocPK(idr));
  }

  public String getSenderNameAndSurnameByIDR(String idr) {
    return umtPayTransferCloseFacade.getSenderNameAndSurnameByIDR(
        new DboDocPK(idr));
  }

  public String getReceiverNameAndSurnameByIDR(String idr) {
    return umtPayTransferCloseFacade.getReceiverNameAndSurnameByIDR(
        new DboDocPK(idr));
  }

  public int getStatusByIDR(String idr) {
    return umtPayTransferCloseFacade.getStatusByIDR(new DboDocPK(idr));
  }

  public String getAmountWithCurrency(String idr) {
    return umtPayTransferCloseFacade.getAmountWithCurrency(new DboDocPK(idr));
  }
}
