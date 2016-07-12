package com.bssys.jsf.Widget;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

import static com.bssys.jsf.Widget.DashboardWidgetInfo.LAST_TRANSFER_MINI;

@Named(LAST_TRANSFER_MINI)
@ViewScoped
public class LastTransferMiniWidgetController extends LastTransferWidgetController
    implements DashboardWidgetController, Serializable {
  @Override
  protected String getCompositeName() {
    return "lasttransfer_mini";
  }
}
