package com.bssys.jsf.Widget;

import com.bssys.ejb.UMTWebProviderFacade;
import com.bssys.entity.UMTWebProviderHelper;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

@Named("providerConverter")
@ApplicationScoped
public class ProviderConverter implements Converter {

  @Inject
  private UMTWebProviderFacade umtWebProviderFacade;

  @Override
  public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
    if(value != null && value.trim().length() > 0) {
      try {
        return umtWebProviderFacade.getProviderById(Integer.parseInt(value));
      } catch(NumberFormatException e) {
        throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
      }
    }
    else {
      return null;
    }
  }

  @Override
  public String getAsString(FacesContext fc, UIComponent uic, Object object) {
    if(object != null) {
      return String.valueOf(((UMTWebProviderHelper) object).getId());
    }
    else {
      return null;
    }
  }
}
