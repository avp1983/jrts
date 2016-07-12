package com.bssys.jsf;

import com.bssys.ejb.UMTWebProviderCategoriesFacade;
import com.bssys.entity.UMTWebProvCategories;
import com.google.common.base.Strings;
import org.primefaces.component.graphicimage.GraphicImage;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panel.Panel;

import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.IOException;

@FacesComponent(value = "PayLegalCategoryUtil")
public class PayLegalCategoryUtil extends Panel implements NamingContainer {

  private Panel widgetPanel;
  private GraphicImage categoryImage;
  private OutputLabel headerLabel;

  @Inject
  private UMTWebProviderCategoriesFacade webProvCategoriesFacade;

  @Override
  public String getFamily() {
    return UINamingContainer.COMPONENT_FAMILY;
  }

  @Override
  public void encodeBegin(FacesContext context) throws IOException {
    int categoryID = Integer.parseInt((String) getAttributes().get("widgetData"));
    UMTWebProvCategories category = webProvCategoriesFacade.getCategoryByID(categoryID);

    headerLabel.setValue(category.getName());
    if (Strings.isNullOrEmpty(category.getIcoPath())) {
      categoryImage.setRendered(false);
    } else {
      categoryImage.setUrl(category.getIcoPath());
    }

    super.encodeBegin(context);
  }

  public Panel getWidgetPanel() {
    return widgetPanel;
  }

  public void setWidgetPanel(Panel widgetPanel) {
    this.widgetPanel = widgetPanel;
  }

  public GraphicImage getCategoryImage() {
    return categoryImage;
  }

  public void setCategoryImage(GraphicImage categoryImage) {
    this.categoryImage = categoryImage;
  }

  public OutputLabel getHeaderLabel() {
    return headerLabel;
  }

  public void setHeaderLabel(OutputLabel headerLabel) {
    this.headerLabel = headerLabel;
  }
}
