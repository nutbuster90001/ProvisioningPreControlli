package it.athora.provisioningprecontrolli.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import it.athora.provisioningprecontrolli.bo.AgenziaListino;
import it.athora.provisioningprecontrolli.business.ProvisioningPreControlliBusiness;

public class ProvisioningPreControlliBper {
	private static final Logger logger = Logger.getLogger(ProvisioningPreControlliBper.class.getName());

	public static void main(String[] args) {
		try {
			new ProvisioningPreControlliBusiness(AgenziaListino.BPER).execute();
		} catch (Throwable e) {
			logger.log(Level.SEVERE, "Errore in procedura: ", e);
			System.out.println("Errore in procedura");
		}
	}

}
