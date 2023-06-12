package org.mule.extension.customcustom.internal;


/**
 * This class represents an extension connection just as example (there is no real connection with anything here c:).
 */
public final class CurrencyconversioncustomConnection {

  private final String id;

  public CurrencyconversioncustomConnection(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void invalidate() {
    // do something to invalidate this connection!
  }
}
