package it.athora.provisioningprecontrolli.business;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import it.athora.provisioningprecontrolli.bo.Record;
import it.athora.provisioningprecontrolli.utils.PropertiesLoader;

public final class CsvWriter {

	private static final Logger logger = Logger.getLogger(CsvWriter.class.getName());

	public static void writeCsv(Path originalFilePath, String suffix, List<Record> rows)
			throws IOException {
		String originalFileName = originalFilePath.getFileName().toString();
		String newFileName = originalFileName.replace(".csv", "") + suffix;

		if (rows == null || rows.isEmpty()) {
			logger.log(Level.INFO, "Nessun file {0} creato", newFileName);
			return;
		}

		Path outputDir = Paths.get(PropertiesLoader.getOutputFolder());
		Path newFilePath = outputDir.resolve(newFileName);

		List<String> lines = rows.stream().map(Record::toCsvString).collect(Collectors.toList());

		Files.write(newFilePath, lines);
		logger.log(Level.INFO, "Scritti {0} record in {1}", new Object[] { rows.size(), newFilePath });
	}

}
