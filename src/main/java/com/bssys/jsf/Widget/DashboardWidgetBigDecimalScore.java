package com.bssys.jsf.Widget;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

public class DashboardWidgetBigDecimalScore extends DashboardWidgetScore {

  public DashboardWidgetBigDecimalScore(String scoreName, int userKey, Callable<Object> updateFunc) {
    super(scoreName, userKey, updateFunc);
  }

  @Override
  public boolean isChanged() {
    return ((BigDecimal) (this.get())).intValue() != ((BigDecimal) (this.getNewValue())).intValue();
  }
}
