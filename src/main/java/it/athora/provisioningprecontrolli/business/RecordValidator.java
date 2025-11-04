package it.athora.provisioningprecontrolli.business;

import it.athora.provisioningprecontrolli.bo.AgenziaListino;
import it.athora.provisioningprecontrolli.bo.LunghezzaCampo;
import it.athora.provisioningprecontrolli.bo.Record;
import it.athora.provisioningprecontrolli.utils.TextUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RecordValidator {

  private static final Logger logger = LoggerFactory.getLogger(RecordValidator.class);

  private static final Pattern NON_ASCII_PATTERN = Pattern.compile("[^\\p{ASCII}]");
  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^(?!.*\\.\\.)[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

  private final List<LogPredicate> checks;

  private RecordValidator(List<LogPredicate> checks) {
    this.checks = Collections.unmodifiableList(checks);
  }

  public static RecordValidator from(AgenziaListino agenziaListino) {
    List<LogPredicate> checks =
        Arrays.asList(
            new LogPredicate(
                record -> !record.isMalformed(),
                record -> "Record '" + record.getOriginalLine() + "' formato non valido"),
            new LogPredicate(
                record -> TextUtils.allNotBlank(record.getMandatoryFieldsAsList()),
                record -> "Record '" + record.getOriginalLine() + "' contiene campi vuoti"),
            new LogPredicate(
                record -> {
                  Map<String, String> fieldsMap = record.getFieldsAsMap();
                  for (String nomeCampo : fieldsMap.keySet()) {
                    String valoreCampo = fieldsMap.get(nomeCampo);
                    if (LunghezzaCampo.isCampoTroppoLungo(nomeCampo, valoreCampo)) {
                      return false;
                    }
                  }
                  return true;
                },
                record -> "Record '" + record.getOriginalLine() + "' contiene campi troppo lunghi"),
            new LogPredicate(
                record -> !NON_ASCII_PATTERN.matcher(record.getOriginalLine()).find(),
                record -> "Record '" + record.getOriginalLine() + "' contiene caratteri non-ASCII"),
            new LogPredicate(
                record -> EMAIL_PATTERN.matcher(record.getEmail()).matches(),
                record -> "Record '" + record.getOriginalLine() + "' email non valida"),
            new LogPredicate(
                record -> agenziaListino.isAgenziaValid(record.getAgenzia()),
                record -> "Record '" + record.getOriginalLine() + "' agenzia non valida"),
            new LogPredicate(
                record -> agenziaListino.isListinoValid(record.getListino()),
                record -> "Record '" + record.getOriginalLine() + "' listino non valido"),
            new LogPredicate(
                record -> {
                  try {
                    LocalDate.parse(record.getDataInizio(), DATE_FORMATTER);
                  } catch (DateTimeParseException e) {
                    return false;
                  }
                  return true;
                },
                record -> "Record '" + record.getOriginalLine() + "' data inizio non valida"),
            new LogPredicate(
                record -> {
                  if (TextUtils.isBlank(record.getDataFine())) return true;
                  try {
                    LocalDate.parse(record.getDataFine(), DATE_FORMATTER);
                  } catch (DateTimeParseException e) {
                    return false;
                  }
                  return true;
                },
                record -> "Record '" + record.getOriginalLine() + "' data fine non valida"));

    return new RecordValidator(checks);
  }

  public boolean validate(Record record) {
    for (LogPredicate check : checks) {
      if (!check.test(record)) {
        logger.warn(check.logMessage(record));
        return false;
      }
    }
    return true;
  }

  private static final class LogPredicate {
    private final Predicate<Record> predicate;
    private final Function<Record, String> logMsg;

    public LogPredicate(Predicate<Record> predicate, Function<Record, String> logMsg) {
      this.predicate = predicate;
      this.logMsg = logMsg;
    }

    public boolean test(Record record) {
      return predicate.test(record);
    }

    public String logMessage(Record record) {
      return logMsg.apply(record);
    }
  }
}
