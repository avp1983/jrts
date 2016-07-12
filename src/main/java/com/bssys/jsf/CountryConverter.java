package com.bssys.jsf;

import com.bssys.ejb.CountryFacade;
import com.bssys.entity.Country;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

@Named("countryConverter")
@ApplicationScoped
public class CountryConverter implements Converter {
  @Inject
  CountryFacade countryFacade;

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
    return countryFacade.getByCode(value);
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
    if (value == null) {
      return null;
    }
    return ((Country) value).getCode();
  }
}
