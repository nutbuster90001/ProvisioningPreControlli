package it.athora.provisioningprecontrolli.business;

import it.athora.provisioningprecontrolli.bo.AgenziaListino;
import it.athora.provisioningprecontrolli.bo.Record;
import it.athora.provisioningprecontrolli.utils.PropertiesLoader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProvisioningPreControlliBusiness {

  private static final Logger logger =
      LoggerFactory.getLogger(ProvisioningPreControlliBusiness.class);

  private AgenziaListino agenziaListino;

  public ProvisioningPreControlliBusiness(AgenziaListino agenziaListino) {
    this.agenziaListino = agenziaListino;
    logger.info("Intermediario: {}", this.agenziaListino.getNome());
  }

  @SuppressWarnings("unused")
  private ProvisioningPreControlliBusiness() {}

  public void execute() throws Throwable {
    String inputPath = PropertiesLoader.getInputFolder();
    try (Stream<Path> paths = Files.list(Paths.get(inputPath))) {
      List<Path> csvFiles =
          paths.filter(path -> path.toString().endsWith(".csv")).collect(Collectors.toList());

      if (csvFiles.isEmpty()) {
        logger.info("Nessun file CSV trovato in input {}", inputPath);
        return;
      }

      for (Path csvFile : csvFiles) {
        logger.info("Inizio controllo file {}", csvFile.getFileName());
        processFile(csvFile);
        logger.info("Fine controllo file {}", csvFile.getFileName());
      }

    } catch (IOException e) {
      throw e;
    }
  }

  private void processFile(Path filePath) throws IOException {

    List<Record> records =
        Files.lines(filePath)
            .map(
                line -> {
                  String[] fields = line.split(PropertiesLoader.getCsvDelimiter(), -1);
                  if (fields.length < 10) {
                    return new Record(line);
                  }
                  return new Record(line, fields);
                })
            .collect(Collectors.toList());

    RecordValidator validator = RecordValidator.from(agenziaListino);
    Map<Boolean, List<Record>> partitionedRows =
        records.stream().collect(Collectors.partitioningBy(validator::validate));

    List<Record> okRecords = partitionedRows.get(true);
    List<Record> koRecords = partitionedRows.get(false);

    if (okRecords != null && !okRecords.isEmpty())
      logger.info("File {} , record OK: {}", filePath.getFileName(), okRecords.size());

    if (koRecords != null && !koRecords.isEmpty())
      logger.warn("File {} , record KO: {}", filePath.getFileName(), koRecords.size());

    CsvWriter.writeCsv(filePath, "_ok.csv", okRecords);
    CsvWriter.writeCsv(filePath, "_ko.csv", koRecords);
  }
}
