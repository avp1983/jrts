package com.bssys.ejb.creditpilot;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

public class ParserValidator implements ValidationEventHandler {
  private StringBuilder sb;

  public ParserValidator() {
    sb = new StringBuilder();
  }

  public StringBuilder getSb() {
    return sb;
  }

  public boolean handleEvent(ValidationEvent event) {
    if (event.getMessage() != null) {
      if (event.getMessage().contains("unexpected element")) {
        return true;
      }
    }

    sb.append("Parse error\n");
    sb.append(String.format("Message: %s\n", event.getMessage()));
    sb.append(String.format("Linked exception: %s\n", event.getLinkedException()));
    sb.append(String.format("At line %d, column %d, offset %d\n\n",
        event.getLocator().getLineNumber(),
        event.getLocator().getColumnNumber(),
        event.getLocator().getOffset()));
    return false;
  }
}
