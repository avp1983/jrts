package com.bssys.jsf;

import com.bssys.bls.types.DboDocPK;
import com.bssys.ejb.FreeAgExDocFacade;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;

@Named("freeAgExDocUtil")
@ApplicationScoped
public class FreeAgExDocUtil implements Serializable {

  @Inject
  FreeAgExDocFacade freeAgExDocFacade;

  public int getStatusByIDR(String idr) {
    return freeAgExDocFacade.getStatusByIDRForUser(new DboDocPK(idr));
  }

  public String getDocumentNumberByIDR(String idr) {
    return freeAgExDocFacade.getDocumentNumberByIDR(new DboDocPK(idr));
  }

  public Date getDocumentDateByIDR(String idr) {
    return freeAgExDocFacade.getDocumentDateByIDR(new DboDocPK(idr));
  }

  public String getFreeDocTypeDescrByIDR(String idr) {
    return freeAgExDocFacade.getFreeDocTypeDescrByIDR(new DboDocPK(idr));
  }
}
