package com.bssys.jsf.Widget;

import com.bssys.ejb.UmtPayLegalFacade;
import com.bssys.ejb.UmtPayTransferOpenFacade;
import com.bssys.entity.PayTransferHelper;
import com.bssys.lang.Date.DatesUtils;
import com.bssys.session.UserSessionBean;
import com.bssys.umt.Statuses;
import com.sun.tools.javac.util.Pair;
import org.apache.commons.lang.time.DateUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.bssys.entity.UmtDocType.PayLegal;
import static com.bssys.entity.UmtDocType.PayTransferOpen;
import static com.bssys.jsf.Widget.DashboardWidgetInfo.LAST_TRANSFER;

@Named(LAST_TRANSFER)
@RequestScoped
public class LastTransferWidgetController extends StatelessCompositeWidget {
  @Inject
  private UmtPayTransferOpenFacade umtPayTransferOpenFacade;

  @Inject
  private UmtPayLegalFacade umtPayLegalFacade;

  private static final int LAST_TRANSFER_COUNT = 5;
  private List<Object[]> lastRequests;

  @Inject
  private UserSessionBean userSession;

  @Override
  protected Pair<String, String> getLocResHeader() {
    return Pair.of("lasttransfer", "LAST_TRANSFERS");
  }

  @Override
  protected String getCompositeName() {
    return "lasttransfer";
  }

  @Override
  protected String getStyleClass() {
    return "ui-widget-scroller";
  }

  @PostConstruct
  private void initBean(){
    lastRequests = umtPayTransferOpenFacade.getLastSendTransfers(LAST_TRANSFER_COUNT, userSession.getUserKey());
  }

  public List<Object[]> getLastRequests() {
    return lastRequests;
  }

  public String getLastOperationDescriptionByStatus(int docStatus) {
    switch (docStatus) {
      case Statuses.STS_PAID:
        return userSession.getLocRes("lasttransfer", "LAST_TRANSFERS_PAID");
      case Statuses.STS_RETURNED:
        return userSession.getLocRes("lasttransfer", "LAST_TRANSFERS_RETURN");
      default:
        return userSession.getLocRes("lasttransfer", "LAST_TRANSFERS_SEND");
    }
  }

  public String getFormatDocumentDate(PayTransferHelper doc) {
    DateFormat dateFormat;
    Date docLastModificationDate = null;
    if (doc.getDocType() == PayTransferOpen) {
      docLastModificationDate = umtPayTransferOpenFacade.getDocLastModificationDateByIDR(doc.getDocPK(),
          userSession.getUserKey());
    } else if (doc.getDocType() == PayLegal) {
      docLastModificationDate = doc.getDocumentDate();
    }

    if (DateUtils.isSameDay(docLastModificationDate, new Date())) {
      dateFormat = new SimpleDateFormat("HH:mm");
    } else {
      return DatesUtils.formatAbbr(docLastModificationDate, userSession.getUserLocale());
    }

    return dateFormat.format(docLastModificationDate);
  }

  public String getDocPK(PayTransferHelper doc) {
    return doc.getDocPK().toDelimString();
  }

}
