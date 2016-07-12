package com.bssys.ejb;

import com.bssys.entity.DashboardWidgetModel;
import com.bssys.test.DataTools;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.primefaces.model.DashboardModel;

import javax.inject.Inject;
import java.util.HashMap;

import static com.bssys.jsf.Widget.DashboardWidgetInfo.PAY_TRANSFER_OPEN;

@Ignore
@RunWith(Arquillian.class)
public class UserInterfaceStateFacadeTest {
    @Inject
    UserInterfaceStateFacade facade;

    @BeforeClass
    public static void setUp() throws Exception {
        DataTools.createDBStructures("REMOTEUSER.xml");
        DataTools.populateData("REMOTEUSER-data.xml");
    }

    @Test
    public void testGetTaskBarStateFromDB() throws Exception {
        Assert.assertNull(facade.getTaskBarStateFromDB(1000000));
        DashboardState state = facade.getTaskBarStateFromDB(292);
        Assert.assertNotNull(state);
        Assert.assertTrue(state.getDashboardModel() != null);
        state = facade.getTaskBarStateFromDB(301);
        Assert.assertNotNull(state);
        Assert.assertNotNull(state.getDashboardModel());
    }

    @Test
    public void testGetMainPageDashboardFromDB() throws Exception {
        Assert.assertNull(facade.getMainPageDashboardFromDB(1000000));
        Assert.assertNull(facade.getMainPageDashboardFromDB(292));
        DashboardState state = facade.getMainPageDashboardFromDB(301);
        Assert.assertNotNull(state);
        DashboardModel dmodel = state.getDashboardModel();
        Assert.assertNotNull(dmodel);
    }

    @Test
    public void testSaveTaskBarToDB() throws Exception {
      DashboardState state;
        state = facade.getTaskBarStateFromDB(292);
        Assert.assertNull(state);
        state = new DashboardState();
        state.getWidgetModels().put("test1", new DashboardWidgetModel(PAY_TRANSFER_OPEN, "11|33333|55555"));
        facade.saveTaskBarToDB(292,state);
        state = facade.getTaskBarStateFromDB(292);
        Assert.assertNotNull(state);
        HashMap<String, DashboardWidgetModel> widgetsData = state.getWidgetModels();
        String data = (String) widgetsData.get("test1").getData();
        Assert.assertEquals("11|33333|55555", data);
    }

    @Test
    public void testSaveMainPageDashboardToDB() throws Exception {
        DashboardState state;
        state = facade.getMainPageDashboardFromDB(292);
        Assert.assertNull(state);
        state = new DashboardState();
        state.getWidgetModels().put("test1", new DashboardWidgetModel(PAY_TRANSFER_OPEN,"11|33333|55555"));
        facade.saveMainPageDashboardToDB(292, state);
        state = facade.getMainPageDashboardFromDB(292);
        Assert.assertNotNull(state);
        HashMap<String, DashboardWidgetModel> widgetsData = state.getWidgetModels();
        DashboardWidgetModel widgetModelFromDB = widgetsData.get("test1");
        String data = (String) widgetModelFromDB.getData();
        Assert.assertEquals("11|33333|55555", data);
    }
}
