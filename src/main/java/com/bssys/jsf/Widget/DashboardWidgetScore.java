package com.bssys.jsf.Widget;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.util.concurrent.Callable;

public class DashboardWidgetScore {

  private final String scoreName;
  private Object newValue;
  private final int userKey;
  private final Callable<Object> update;

  public DashboardWidgetScore(String scoreName, int userKey, Callable<Object> updateFunc) {
    this.scoreName = scoreName;
    this.userKey = userKey;
    this.update = updateFunc;
    try {
      this.updateData();
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (this.get() == null) {
      this.reset();
    }
  }

  private Cache getCache() {
    ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    return (Cache) servletContext.getAttribute("globalCache");
  }

  private String getKey() {
    return this.scoreName.toUpperCase() + this.userKey;
  }

  public void put(Object value) {
    getCache().put(new Element(this.getKey(), value));
  }

  public Object get() {
    Element result = this.getCache().get(this.getKey());
    if (result == null) {
      return null;
    }
    return result.getObjectValue();
  }

  public boolean isChanged() {
    return null != newValue && !(this.newValue.equals(this.get()));
  }

  public void reset() {
    this.put(this.newValue);
  }

  public Object getNewValue() {
    return this.newValue;
  }

  public void updateData() {
    if (this.update != null) {
      try {
        this.newValue = this.update.call();
      } catch (Exception e) {
        e.printStackTrace();
      }

    }
  }

}
