package it.athora.provisioningprecontrolli.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PropertiesLoader {

  private static final Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);
  private static final Properties configProp = new Properties();

  static {
    loadProperties();
  }

  private PropertiesLoader() {}

  public static String getInputFolder() {
    String inputFolderPath = getConfigProperty(PropertyKeys.INPUT_FOLDER);
    folderPathValidatorAndNormalize(inputFolderPath);
    return inputFolderPath;
  }

  public static String getOutputFolder() {
    String outputFolderPath = getConfigProperty(PropertyKeys.OUTPUT_FOLDER);
    folderPathValidatorAndNormalize(outputFolderPath);
    return outputFolderPath;
  }

  public static String getCsvDelimiter() {
    String delimiter = getConfigProperty(PropertyKeys.CSV_DELIMITER);
    if (delimiter.length() != 1
        || Character.isWhitespace(delimiter.charAt(0))
        || delimiter.charAt(0) == '\n'
        || delimiter.charAt(0) == '\r') {
      throw new IllegalStateException(
          "csv.delimiter deve essere un carattere visibile e non uno spazio o newline: '"
              + delimiter
              + "'");
    }
    return delimiter;
  }

  private static void folderPathValidatorAndNormalize(String folderPathStr) {
    Path folderPath = Paths.get(folderPathStr).toAbsolutePath().normalize();

    if (!Files.exists(folderPath)) {
      throw new IllegalStateException("Il percorso specificato non esiste: " + folderPath);
    }
    if (!Files.isDirectory(folderPath)) {
      throw new IllegalStateException("Il percorso specificato non � una cartella: " + folderPath);
    }
  }

  private static void loadProperties() {
    Path path = Paths.get("properties", "provisioningprecontrolli.properties");

    if (!Files.exists(path)) {
      throw new IllegalStateException(
          "File di properties " + path.toAbsolutePath() + " non trovato");
    }

    try (InputStream inputStream = Files.newInputStream(path)) {
      configProp.load(inputStream);
      logger.info("Lettura properties da {}", path.toAbsolutePath());
    } catch (IOException e) {
      throw new IllegalStateException(
          "File di properties " + path.toAbsolutePath() + " non trovato");
    }
  }

  private static String getConfigProperty(String key) {
    String value = configProp.getProperty(key);

    if (TextUtils.isBlank(value)) {
      throw new IllegalStateException(
          "Valore mancante o vuoto per la propriet� obbligatoria " + key);
    }
    return value;
  }

  private static final class PropertyKeys {

    public static final String INPUT_FOLDER = "input.folder";
    public static final String OUTPUT_FOLDER = "output.folder";
    public static final String CSV_DELIMITER = "csv.delimiter";

    private PropertyKeys() {}
  }
}
