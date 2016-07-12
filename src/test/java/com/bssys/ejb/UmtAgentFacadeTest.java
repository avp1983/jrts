package com.bssys.ejb;

import com.bssys.test.DataTools;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@Ignore
@RunWith(Arquillian.class)
public class UmtAgentFacadeTest extends Assert {
    @Inject
    private UmtAgentFacade facade;

    @BeforeClass
    public static void setUpClass() throws Exception {
        DataTools.createDBStructures("COUNTRY.xml");
        DataTools.createDBStructures("CUSTOMER.xml");
        DataTools.createDBStructures("POSTCLNT.xml");
        DataTools.createDBStructures("UMTAGENTLINKS.xml");
        DataTools.createDBStructures("UMTTRANSFERSYSDATA.xml");
        DataTools.createDBStructures("UMTAGENTTOWNS.xml");
        DataTools.createDBStructures("REMOTEIDS.xml");
        DataTools.createDBStructures("VIEWCUSTANDPOINTS.xml");

        DataTools.populateData("COUNTRY-data.xml");
        DataTools.populateData("CUSTOMER-data.xml");
        DataTools.populateData("POSTCLNT-data.xml");
        DataTools.populateData("UMTAGENTLINKS-data.xml");
        DataTools.populateData("UMTTRANSFERSYSDATA-data.xml");
        DataTools.populateData("UMTAGENTTOWNS-data.xml");
        DataTools.populateData("REMOTEIDS-data.xml");
    }

}
