package it.athora.provisioningprecontrolli.core;

import it.athora.provisioningprecontrolli.bo.AgenziaListino;
import it.athora.provisioningprecontrolli.business.ProvisioningPreControlliBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProvisioningPreControlliBancaDelFucino {
  private static final Logger logger =
      LoggerFactory.getLogger(ProvisioningPreControlliBancaDelFucino.class);

  public static void main(String[] args) {
    try {
      new ProvisioningPreControlliBusiness(AgenziaListino.BANCADELFUCINO).execute();
    } catch (Throwable e) {
      logger.error("Errore in procedura", e);
      System.out.println("Errore in procedura");
    }
  }
}
