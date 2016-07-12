package com.bssys.umt;

import javax.ejb.ApplicationException;

/**
 * Created by lukas on 04.12.2014.
 */
@ApplicationException(rollback=true)
public class UpdateException extends Exception {

  public UpdateException() {
    super();
  }

  public UpdateException(String message) {
    super(message);
  }
}
