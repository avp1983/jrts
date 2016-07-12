package com.bssys.jsf;

import com.bssys.bls.types.DboDocPK;
import com.bssys.ejb.FreeClientDocFacade;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;

@Named("freeClientDocUtil")
@ApplicationScoped
public class FreeClientDocUtil implements Serializable {

  @Inject
  FreeClientDocFacade freeClientDocFacade;

  public int getStatusByIDR(String idr) {
    return freeClientDocFacade.getStatusByIDRForUser(new DboDocPK(idr));
  }

  public String getDocumentNumberByIDR(String idr) {
    return freeClientDocFacade.getDocumentNumberByIDR(new DboDocPK(idr));
  }

  public Date getDocumentDateByIDR(String idr) {
    return freeClientDocFacade.getDocumentDateByIDR(new DboDocPK(idr));
  }

  public String getFreeDocTypeDescrByIDR(String idr) {
    return freeClientDocFacade.getFreeDocTypeDescrByIDR(new DboDocPK(idr));
  }
}
