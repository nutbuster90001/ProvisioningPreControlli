package it.athora.provisioningprecontrolli.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.athora.provisioningprecontrolli.bo.AgenziaListino;
import it.athora.provisioningprecontrolli.business.ProvisioningPreControlliBusiness;

public class ProvisioningPreControlliFacileit {
        private static final Logger logger = LoggerFactory.getLogger(ProvisioningPreControlliFacileit.class);

	public static void main(String[] args) {
		try {
			new ProvisioningPreControlliBusiness(AgenziaListino.FACILEIT).execute();
		} catch (Throwable e) {
                        logger.error("Errore in procedura", e);
			System.out.println("Errore in procedura");
		}
	}

}
