package it.athora.provisioningprecontrolli.bo;

import java.util.Arrays;

public enum AgenziaListino {
  BARI(
      "Bari",
      new String[] {"001818", "001819", "1818", "1819"},
      new String[] {"FilialeBanca", "DirezioneBanca"}),
  BPER(
      "Bper",
      new String[] {"001800", "001804", "1800", "1804"},
      new String[] {"FILIALISTA Banca Bper"}),
  FACILEIT("Facile.it", new String[] {"005101", "5101"}, new String[] {"BROKER FACILE"}),
  SCM("SCM", new String[] {"001820", "1820"}, new String[] {"BROKER VITA"}),
  VITANUOVA("Vita Nuova", new String[] {"005153", "5153"}, new String[] {"BROKER VITA"});

  private final String nome;
  private final String[] agenzie;
  private final String[] listini;

  AgenziaListino(String nome, String[] agenzie, String[] listini) {
    this.nome = nome;
    this.agenzie = agenzie;
    this.listini = listini;
  }

  public boolean isAgenziaValid(String agenzia) {
    return Arrays.asList(this.agenzie).contains(agenzia);
  }

  public boolean isListinoValid(String listino) {
    return Arrays.asList(this.listini).contains(listino);
  }

  public String getNome() {
    return nome;
  }
}
