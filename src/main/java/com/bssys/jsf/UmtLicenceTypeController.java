package com.bssys.jsf;

import com.bssys.ejb.UmtLicenceTypeFacade;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

import static com.bssys.umt.TransferSys.TRANSFER_SYS_CREDIT_PILOT;

@Named("umtLicenceTypeController")
@ApplicationScoped
public class UmtLicenceTypeController {
  @Inject
  UmtLicenceTypeFacade umtLicenceTypeFacade;

  public List<String> findByNameForCreditPilot(String query) {
    return umtLicenceTypeFacade.getNameLike(query, TRANSFER_SYS_CREDIT_PILOT);
  }
}
