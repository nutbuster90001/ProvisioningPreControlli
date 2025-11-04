package it.athora.provisioningprecontrolli.business;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

import it.athora.provisioningprecontrolli.bo.Record;
import it.athora.provisioningprecontrolli.utils.PropertiesLoader;

public final class CsvWriter {

        private static final Logger logger = LoggerFactory.getLogger(CsvWriter.class);

	public static void writeCsv(Path originalFilePath, String suffix, List<Record> rows)
			throws IOException {
		String originalFileName = originalFilePath.getFileName().toString();
		String newFileName = originalFileName.replace(".csv", "") + suffix;

		if (rows == null || rows.isEmpty()) {
                        logger.info("Nessun file {} creato", newFileName);
			return;
		}

		Path outputDir = Paths.get(PropertiesLoader.getOutputFolder());
		Path newFilePath = outputDir.resolve(newFileName);

		List<String> lines = rows.stream().map(Record::toCsvString).collect(Collectors.toList());

		Files.write(newFilePath, lines);
                logger.info("Scritti {} record in {}", rows.size(), newFilePath);
	}

}
