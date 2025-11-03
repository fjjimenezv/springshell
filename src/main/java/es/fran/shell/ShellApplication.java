package es.fran.shell;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import es.fran.shell.exception.ShellException;
import es.fran.shell.model.ShellConstants;
import es.fran.shell.services.CSVService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@SpringBootApplication
public class ShellApplication {

	@Autowired
	CSVService csvService;	

	public static void main(String[] args) {
		try {
			System.out.println("Version 20251103_0802");
			// Backup del fichero actual
			String os = System.getProperty("os.name").toLowerCase();
			String hostname = InetAddress.getLocalHost().getHostName();
			System.out.println("Hostname: " + hostname + ", OS: " + os);			
 
			String strRutaBackup = null;
			String strRutaOneDriveCSV = null;

			if (os.contains("win")) {
				strRutaBackup = "C:/Users/fjimenez/OneDrive/Documentos/Home/backup";
				strRutaOneDriveCSV = "C:/Users/fjimenez/OneDrive/Documentos/Home/Shell/" + ShellConstants.SHELL_OBJETOS_CSV;
			} else {
				throw new ShellException("Sistema operativo no soportado");
			}

			BasicFileAttributes attributesCSV = Files.readAttributes(Paths.get(strRutaOneDriveCSV), BasicFileAttributes.class);
			LocalDateTime fechaArchivoCSV = attributesCSV.lastModifiedTime().toInstant().atZone(ZoneId.systemDefault())
					.toLocalDateTime();
			Files.copy(
					Paths.get(strRutaOneDriveCSV),
					Paths.get(strRutaBackup + "/" + ShellConstants.SHELL_OBJETOS + "_" + fechaArchivoCSV.getYear()
							+ String.format("%02d", fechaArchivoCSV.getMonthValue())
							+ String.format("%02d", fechaArchivoCSV.getDayOfMonth()) + "_"
							+ String.format("%02d", fechaArchivoCSV.getHour())
							+ String.format("%02d", fechaArchivoCSV.getMinute()) + ".csv"),
					StandardCopyOption.REPLACE_EXISTING);

			eliminarAntiguos(strRutaBackup, ShellConstants.SHELL_OBJETOS + "_", 10);			
		} catch (ShellException | IOException e) {
			e.printStackTrace();
		}  		
		SpringApplication.run(ShellApplication.class, args);
	}

	public static void eliminarAntiguos(String strRutaBackup, String strPrefix, int lastNElements) throws IOException {
		// Eliminación de los ficheros antiguos
		List<Path> files = new ArrayList<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(strRutaBackup))) {
			for (Path p : stream) {
				if (p.toFile().getName().startsWith(strPrefix)) {
					files.add(p);
				}
			}
		}

		if (files.size() > 1) {
			Collections.sort(files, new Comparator<Path>() {
				public int compare(Path o1, Path o2) {
					try {
						return Files.getLastModifiedTime(o1).compareTo(Files.getLastModifiedTime(o2));
					} catch (IOException e) {
						return 0;
					}
				}
			});
		}

		int total = files.size();
		int eliminar = 0;
		if (total > lastNElements) {
			eliminar = total - lastNElements;
		}
		for (Path file : files) {
			if (eliminar > 0) {
				Files.deleteIfExists(file);
				eliminar--;
			}
		}
	}

	@PostConstruct
	public void doSomethingOnPostConstruct() throws IOException {
		System.out.println("Callback triggered - @PostConstruct");
		csvService.readCSV();
	}	

	@PreDestroy
    public void destroy() throws IOException {
        System.out.println(
          "Callback triggered - @PreDestroy.");
		// csvService.writeCSV();
		/**
		Files.copy(
				Paths.get(strRutaOneDriveCSV),
				Paths.get(strRutaICloud),
				StandardCopyOption.REPLACE_EXISTING);		
		Files.copy(
				Paths.get(strRutaOneDriveCSV),
				Paths.get(strRutaGoogle),
				StandardCopyOption.REPLACE_EXISTING);
				*/
    }	

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
    	System.out.println("Callback triggered - ApplicationReadyEvent");
	}		
}
