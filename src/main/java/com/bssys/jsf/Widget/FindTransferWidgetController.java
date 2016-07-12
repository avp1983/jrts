package com.bssys.jsf.Widget;

import com.sun.tools.javac.util.Pair;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import static com.bssys.jsf.Widget.DashboardWidgetInfo.FIND_TRANSFER;

@Named(FIND_TRANSFER)
@ViewScoped
public class FindTransferWidgetController extends StatelessCompositeWidget {
  private String checkNumber;

  @Override
  protected Pair<String, String> getLocResHeader() {
    return Pair.of("findtransfer", "FIND_TRANSFER_BY_KNP");
  }

  @Override
  protected String getCompositeName() {
    return "findtransfer";
  }

  @Override
  protected String getStyleClass() {
    return "";
  }

  public String getCheckNumber() {
    return checkNumber;
  }

  public void setCheckNumber(final String checkNumber) {
    this.checkNumber = checkNumber;
  }
}
