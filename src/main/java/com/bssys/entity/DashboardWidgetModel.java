package com.bssys.entity;

import org.apache.commons.lang.ObjectUtils;

import java.io.Serializable;

public class DashboardWidgetModel implements Serializable {
  private final String type;
  private final Object data;
  private transient boolean isRenderFail;

  public DashboardWidgetModel(String type, Object data) {
    this.type = type;
    this.data = data;
    setRenderFail(false);
  }

  public String getType() {
    return type;
  }

  public Object getData() {
    return data;
  }

  @Override
  public boolean equals(final Object object) {
    if (!(object instanceof DashboardWidgetModel)) {
      return false;
    }
    DashboardWidgetModel other = (DashboardWidgetModel) object;
    return (ObjectUtils.equals(type, other.type) && ObjectUtils.equals(this.data, other.data));
  }

  @Override
  public int hashCode() {
    return this.type.hashCode() + this.data.hashCode();
  }

  public boolean isRenderFail() {
    return isRenderFail;
  }

  public void setRenderFail(boolean isRenderFail) {
    this.isRenderFail = isRenderFail;
  }
}
