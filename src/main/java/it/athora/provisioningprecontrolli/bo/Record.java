package it.athora.provisioningprecontrolli.bo;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.athora.provisioningprecontrolli.utils.PropertiesLoader;
import it.athora.provisioningprecontrolli.utils.TextUtils;

public final class Record {

	private String email;
	private String nome;
	private String cognome;
	private String dataInizio;
	private String dataFine;
	private String codice;
	private String compagnia;
	private String agenzia;
	private String subagenzia;
	private String listino;
	private final String originalLine;
	private final boolean isMalformed;

	public Record(final String originalLine, String[] fields) {
		this.originalLine = originalLine;
		this.isMalformed = false;
		this.email = fields[0];
		this.nome = fields[1];
		this.cognome = fields[2];
		this.dataInizio = fields[3];
		this.dataFine = fields[4];
		this.codice = fields[5];
		this.compagnia = fields[6];
		this.agenzia = fields[7];
		this.subagenzia = fields[8];
		this.listino = fields[9];
	}

	public Record(final String originalLine) {
		this.originalLine = originalLine;
		this.isMalformed = true;
	}

	public String getEmail() {
		return email;
	}

	public String getDataInizio() {
		return dataInizio;
	}

	public String getDataFine() {
		return dataFine;
	}

	public String getAgenzia() {
		return agenzia;
	}

	public String getListino() {
		return listino;
	}

	public boolean isMalformed() {
		return this.isMalformed;
	}

	public String getOriginalLine() {
		return originalLine;
	}

	@Override
	public String toString() {
		return "ProvisioningRow [email=" + email + ", nome=" + nome + ", cognome=" + cognome + ", dataInizio=" + dataInizio
				+ ", dataFine=" + dataFine + ", codice=" + codice + ", compagnia=" + compagnia + ", agenzia=" + agenzia
				+ ", subagenzia=" + subagenzia + ", listino=" + listino + "]";
	}

	public String toCsvString() {
		return TextUtils.isNotBlank(this.originalLine) ? this.originalLine
				: String.join(PropertiesLoader.getCsvDelimiter(), getFieldsAsList());
	}

	public List<String> getFieldsAsList() {
		return Arrays.asList(email, nome, cognome, dataInizio, dataFine, codice, compagnia, agenzia, subagenzia, listino);
	}
	
	public List<String> getMandatoryFieldsAsList(){
		return Arrays.asList(email, nome, cognome, dataInizio, agenzia, listino);
	}

    public Map<String, String> getFieldsAsMap() {
        return Arrays.stream(this.getClass().getDeclaredFields())
            .peek(field -> field.setAccessible(true))
            .collect(Collectors.toMap(
                Field::getName,
                field -> {
                    try {
                        Object value = field.get(this);
                        return value != null ? String.valueOf(value) : null;
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Impossibile accedere al campo", e);
                    }
                }
            ));
    }	

}
