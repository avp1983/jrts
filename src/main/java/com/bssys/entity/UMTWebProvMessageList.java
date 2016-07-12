package com.bssys.entity;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;

@XmlRootElement(name = "kp-dealer")
@XmlAccessorType(XmlAccessType.NONE)
public class UMTWebProvMessageList {

  @XmlElement(name = "message")
  List<UMTWebProvMessage> messages;

  public List<UMTWebProvMessage> getMessages() {
    return messages;
  }

  public void setMessages(List<UMTWebProvMessage> messages) {
    this.messages = messages;
  }

  public void afterUnmarshal(Unmarshaller u, Object parent) {
    if (messages != null) {
      Collections.sort(messages);
    }
  }
}
