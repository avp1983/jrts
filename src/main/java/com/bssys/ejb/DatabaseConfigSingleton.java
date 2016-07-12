package com.bssys.ejb;

import com.bssys.utils.persistence.JpaMetadataBean;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import static com.bssys.entity.PayLegal.UmtPayLegal.GENERATOR_PAY_LEGAL;

@Singleton
@Startup
public class DatabaseConfigSingleton {

  @Inject
  private JpaMetadataBean metadataBean;

  @PostConstruct
  public void initDB() {
    metadataBean.setUpGenerator(GENERATOR_PAY_LEGAL, 0);
  }


}
