package com.bssys.jsf;

import com.bssys.ejb.DashboardState;
import com.bssys.ejb.UMTWebProviderCategoriesFacade;
import com.bssys.ejb.UMTWebProviderFacade;
import com.bssys.entity.UMTWebProviderCategoryHelper;
import com.bssys.entity.UMTWebProviderHelper;
import com.bssys.process.step.ProcessStepResult;
import com.bssys.process.step.ProcessStepResultController;
import com.bssys.session.UserSessionBean;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.component.dashboard.Dashboard;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.bssys.jsf.Widget.DashboardWidgetInfo.FAVOURITE_PROVIDERS_WIDGET;
import static com.bssys.jsf.Widget.DashboardWidgetInfo.WEB_PROVIDER_CATEGORY_WIDGET;
import static com.bssys.process.step.ProcessStepResultType.PROCESS_RESULT_ERROR;

@Named("webProviderCategoriesController")
@ViewScoped
public class WebProviderCategoriesController extends DashboardController {

  private LinkedHashMap<Integer, List<UMTWebProviderHelper>> providersByCategories = new LinkedHashMap<>();
  private Map<Integer, Boolean> isEmptyProviderCategory = new HashMap<>();

  private String searchString;

  public String getSearchString() {
    return searchString;
  }

  public void setSearchString(String searchString) {
    this.searchString = searchString;
  }

  private ProcessStepResult lastResult;

  @Inject
  private UMTWebProviderCategoriesFacade umtWebProviderCategoriesFacade;

  @Inject
  private UMTWebProviderFacade umtWebProviderFacade;

  @Inject
  private ProcessStepResultController resultController;

  @Inject
  private UserSessionBean userSession;

  @Override
  protected void saveDashboardState(int userKey, DashboardState dashboardState) {
  }

  @Override
  protected DashboardState restoreDashboardState(int userKey) {
    return null;
  }

  @Override
  protected void processDashboardInitialState() {
    DefaultDashboardModel dashboardModel = getDashboardState().getDashboardModel();
    dashboardModel.addColumn(new DefaultDashboardColumn());
    dashboardModel.addColumn(new DefaultDashboardColumn());
    dashboardModel.addColumn(new DefaultDashboardColumn());
    fillDashboard();
  }

  @Override
  protected Dashboard getDashboard() {
    return (Dashboard) FacesContext.getCurrentInstance().getViewRoot().findComponent("categoriesForm:categoriesDashboard");
  }

  @Override
  protected void registerPossibleWidgetControllers() {
    registerWidgetController(WEB_PROVIDER_CATEGORY_WIDGET);
    registerWidgetController(FAVOURITE_PROVIDERS_WIDGET);
  }

  protected void fillDashboard() {
    int x = getDashboardState().getDashboardModel().getColumnCount() - 1;
    int i = 0;
    onSearch();
    if (providersByCategories != null) {
      addWidget(FAVOURITE_PROVIDERS_WIDGET, 0);
      for (Integer categoryID : providersByCategories.keySet()) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("CATEGORY", categoryID.toString());
        if (i == x) {
          addWidget(WEB_PROVIDER_CATEGORY_WIDGET, 1);
        } else {
          addWidget(WEB_PROVIDER_CATEGORY_WIDGET);
        }
        i++;
      }
    }
  }

  public void onSearch() {
    initProvidersByCategories(getSearchString());
    if (providersByCategories.size() == 0) {
      lastResult = new ProcessStepResult(userSession.getLocRes("umtpaylegal", "PROVIDERS_NOT_FOUND"), PROCESS_RESULT_ERROR);
      resultController.processResult(lastResult);
    }
  }

  public void updateGrowl() {
    if (lastResult != null) {
      resultController.processResult(lastResult);
      lastResult = null;
    }
  }

  private void initProvidersByCategories(String searchString) {
    providersByCategories.clear();
    isEmptyProviderCategory.clear();
    String filterStr = umtWebProviderCategoriesFacade.convertSearchStrToFilterStr(searchString);
    for (UMTWebProviderCategoryHelper categoryHelper : umtWebProviderCategoriesFacade.getCategories(filterStr)) {
      List<UMTWebProviderHelper> providersByCategory = umtWebProviderFacade.getProvidersByCategory(categoryHelper.getId(), filterStr);
      providersByCategories.put(categoryHelper.getId(), providersByCategory);
      isEmptyProviderCategory.put(categoryHelper.getId(), CollectionUtils.isEmpty(providersByCategory));
    }
  }

  public List<UMTWebProviderHelper> getProviders(Integer categoryID) {
    if (categoryID == null) {
      return null;
    }
    return providersByCategories.get(categoryID);
  }

  public boolean isRenderCategory(Integer categoryID) {
    if (categoryID == null) {
      return false;
    }
    Boolean isEmptyCategory = isEmptyProviderCategory.get(categoryID);
    return isEmptyCategory != null && !isEmptyCategory;
  }
}
