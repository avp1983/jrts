package com.bssys.jsf.Widget;

import com.google.common.collect.Sets;

import java.util.HashSet;

public class DashboardWidgetInfo {
  public static final String PAY_TRANSFER_OPEN = "payTransferOpenWidget";
  public static final String PAY_TRANSFER_CLOSE = "payTransferCloseWidget";
  public static final String FIND_TRANSFER = "findTransferWidget";
  public static final String LAST_TRANSFER_MINI = "lastTransferMiniWidget";
  public static final String LAST_TRANSFER = "lastTransferWidget";
  public static final String TRANSFER_COUNT = "transferCountWidget";
  public static final String IMPORTANT_WIDGET = "importantWidget";
  public static final String ADDITIONAL_COMMISSION_WIDGET = "additionalCommissionWidget";
  public static final String FREE_CLIENT_DOC = "freeClientDocWidget";
  public static final String FREE_AG_EX_DOC = "freeAgExDocWidget";
  public static final String WEB_PROVIDER_CATEGORY_WIDGET = "webProviderCategoryWidget";
  public static final String PAY_LEGAL = "payLegal";
  public static final String FAVOURITE_PROVIDERS_WIDGET = "favouriteProvidersWidget";

  public static final HashSet<String> TRANSFER_WIDGETS = Sets.newHashSet(PAY_TRANSFER_OPEN, PAY_TRANSFER_CLOSE,
      FIND_TRANSFER, LAST_TRANSFER_MINI, TRANSFER_COUNT);

}
