package it.athora.provisioningprecontrolli.bo;

import it.athora.provisioningprecontrolli.utils.TextUtils;
import java.util.Optional;

public enum LunghezzaCampo {
  EMAIL("email", 255),
  NOME("nome", 50),
  COGNOME("cognome", 50),
  DATA_INIZIO("dataInizio", 10),
  DATA_FINE("dataFine", 10),
  CODICE("codice", 20),
  COMPAGNIA("compagnia", 255),
  AGENZIA("agenzia", 6),
  SUBAGENZIA("subagenzia", 6),
  LISTINO("listino", 255);

  private final String nomeCampo;
  private final int lunghezzaCampo;

  LunghezzaCampo(String nomeCampo, int lunghezzaCampo) {
    this.nomeCampo = nomeCampo;
    this.lunghezzaCampo = lunghezzaCampo;
  }

  public int getLunghezzaCampo() {
    return lunghezzaCampo;
  }

  public static boolean isCampoTroppoLungo(String nomeCampo, String valoreCampo) {
    if (TextUtils.isBlank(valoreCampo)) return false;
    return valoreCampo.length() > lunghezzaCampoFromNomeCampo(nomeCampo);
  }

  private static int lunghezzaCampoFromNomeCampo(String nomeCampo) {
    return fromNomeCampo(nomeCampo)
        .map(LunghezzaCampo::getLunghezzaCampo)
        .orElse(Integer.MAX_VALUE);
  }

  private static Optional<LunghezzaCampo> fromNomeCampo(String nomeCampo) {
    for (LunghezzaCampo b : LunghezzaCampo.values()) {
      if (b.nomeCampo.equalsIgnoreCase(nomeCampo)) {
        return Optional.of(b);
      }
    }
    return Optional.empty();
  }
}
