package it.athora.provisioningprecontrolli.business;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.athora.provisioningprecontrolli.bo.AgenziaListino;
import it.athora.provisioningprecontrolli.bo.Record;
import it.athora.provisioningprecontrolli.utils.PropertiesLoader;

public class ProvisioningPreControlliBusiness {

	private static final Logger logger = Logger.getLogger(ProvisioningPreControlliBusiness.class.getName());

	private AgenziaListino agenziaListino;

	public ProvisioningPreControlliBusiness(AgenziaListino agenziaListino) {
		this.agenziaListino = agenziaListino;
		logger.log(Level.INFO, "Intermediario: {0}", this.agenziaListino.getNome());
	}

	@SuppressWarnings("unused")
	private ProvisioningPreControlliBusiness() {

	}

	public void execute() throws Throwable {
		String inputPath = PropertiesLoader.getInputFolder();
		try (Stream<Path> paths = Files.list(Paths.get(inputPath))) {
			List<Path> csvFiles = paths.filter(path -> path.toString().endsWith(".csv")).collect(Collectors.toList());

			if (csvFiles.isEmpty()) {
				logger.log(Level.INFO, "Nessun file CSV trovato in input {0}", inputPath);
				return;
			}

			for (Path csvFile : csvFiles) {
				logger.log(Level.INFO, "Inizio controllo file {0} ", csvFile.getFileName());
				processFile(csvFile);
				logger.log(Level.INFO, "Fine controllo file {0} ", csvFile.getFileName());
			}

		} catch (IOException e) {
			throw e;
		}
	}

	private void processFile(Path filePath) throws IOException {

		List<Record> records = Files.lines(filePath).map(line -> {
			String[] fields = line.split(PropertiesLoader.getCsvDelimiter(), -1);
			if (fields.length < 10) {
				return new Record(line);
			}
			return new Record(line, fields);
		}).collect(Collectors.toList());

		RecordValidator validator = RecordValidator.from(agenziaListino);
		Map<Boolean, List<Record>> partitionedRows = records.stream()
				.collect(Collectors.partitioningBy(validator::validate));

		List<Record> okRecords = partitionedRows.get(true);
		List<Record> koRecords = partitionedRows.get(false);

		if (okRecords!=null && !okRecords.isEmpty())
			logger.log(Level.INFO, "File {0} , record OK: {1}", new Object[] { filePath.getFileName(), okRecords.size() });
		
		if (koRecords!=null && !koRecords.isEmpty())
			logger.log(Level.WARNING, "File {0} , record KO: {1}", new Object[] { filePath.getFileName(), koRecords.size() });

		CsvWriter.writeCsv(filePath, "_ok.csv", okRecords);
		CsvWriter.writeCsv(filePath, "_ko.csv", koRecords);
	}

}
