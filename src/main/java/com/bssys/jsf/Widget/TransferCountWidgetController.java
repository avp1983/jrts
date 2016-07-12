package com.bssys.jsf.Widget;

import com.sun.tools.javac.util.Pair;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import static com.bssys.jsf.Widget.DashboardWidgetInfo.TRANSFER_COUNT;

@Named(TRANSFER_COUNT)
@ViewScoped
public class TransferCountWidgetController extends StatelessCompositeWidget {

  @Override
  protected Pair<String, String> getLocResHeader() {
    return Pair.of("transfercount", "TRANSFER_COUNT_LIST");
  }

  @Override
  protected String getCompositeName() {
    return "transferCount";
  }

  @Override
  protected String getStyleClass() {
    return "container-transferCountWidget";
  }
}
