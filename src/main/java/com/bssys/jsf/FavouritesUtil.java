package com.bssys.jsf;

import org.primefaces.component.panel.Panel;

import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UINamingContainer;
import javax.inject.Named;

@Named("favouritesUtil")
@FacesComponent(value = "FavouritesUtil")
public class FavouritesUtil extends Panel implements NamingContainer {

  @Override
  public String getFamily() {
    return UINamingContainer.COMPONENT_FAMILY;
  }

}
