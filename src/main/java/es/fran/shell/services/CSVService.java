package es.fran.shell.services;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.fran.shell.model.CSVObject;
import es.fran.shell.model.InventoryObject;
import es.fran.shell.model.PropertyValue;
import es.fran.shell.model.ShellConstants;

@Service
public class CSVService {
	
	@Value("${servidor.path-ficheros}")
	private String pathFicheros;	

	private static List<InventoryObject> objectList = new ArrayList<>(); 
	
	Map<Integer, List<String>> mCategorias = null;
	Map<Integer, Integer> mCategoriasBBDD = null;
	Map<Integer, Pair<String, String>> mCategoriasMetadatos = null;

	public void readCSV() throws IOException {
		FileReader csvObjetos = new FileReader(pathFicheros + "/" + ShellConstants.SHELL_OBJETOS_CSV);
		CSVParser csvParser = new CSVParser(csvObjetos, CSVFormat.DEFAULT);

		csvParser.forEach(x -> {
			InventoryObject fjObject = new InventoryObject();
			String contenedoresAndNameString = x.get(0);
			if (contenedoresAndNameString.contains(":")) {
				fjObject.setName(StringUtils.substringAfterLast(contenedoresAndNameString, ":"));
				fjObject.setContainers(StringUtils.substringBeforeLast(contenedoresAndNameString, ":"));
			} else {
				fjObject.setName(contenedoresAndNameString);
				fjObject.setContainers("");
			}
			if (fjObject.getName().contentEquals("_")) {
				fjObject.setName("");
			}
			String valoresString = x.get(1);
			String[] valoresSplitArray = valoresString.split("\\|");
			for (String valor : valoresSplitArray) {
				String propiedadString = StringUtils.substringBefore(valor, ":").trim();
				String valorString = StringUtils.substringAfter(valor, ":").trim();
				if (StringUtils.isNotEmpty(propiedadString)) {
					fjObject.getPropertyValueList()
							.add(new PropertyValue(
									propiedadString,
									valorString));
				}
			}
			fjObject.setId(Double.valueOf(x.get(2)).intValue());
			fjObject.setParent(Double.valueOf(x.get(3)).intValue());
			objectList.add(fjObject);
		});

		csvParser.close();
	}

	public void writeCSV() throws IOException {
		FileWriter csvObjetos = new FileWriter(pathFicheros + "/" + ShellConstants.SHELL_OBJETOS_CSV);
		CSVPrinter printerObjetos = new CSVPrinter(csvObjetos, CSVFormat.DEFAULT);
		List<CSVObject> csvObjectList = new ArrayList<>();

		for (InventoryObject fjobject : objectList) {
			CSVObject csvObject = new CSVObject();
			csvObject.setId(fjobject.getId());
			csvObject.setParent(fjobject.getParent());
			if (StringUtils.isNotEmpty(fjobject.getName())) {
				if (StringUtils.isNotEmpty(fjobject.getContainers())) {
					csvObject.setContenedoresAndName(fjobject.getContainers() + ":" + fjobject.getName());
				} else {
					csvObject.setContenedoresAndName(fjobject.getName());
				}
			} else {
				csvObject.setContenedoresAndName(fjobject.getContainers() + ":_");
			}

			if (CollectionUtils.isNotEmpty(fjobject.getPropertyValueList())) {
				String strTmp = "";
				for (PropertyValue v : fjobject.getPropertyValueList()) {
					if (strTmp.length() == 0) {
						strTmp = v.getProperty() + ":" + v.getValue();
					} else {
						strTmp = strTmp + " | " + v.getProperty() + ":" + v.getValue();
					}
				}
				csvObject.setValores(strTmp);
			} else {
				csvObject.setValores("");
			}
			csvObjectList.add(csvObject);
		}

		csvObjectList.sort(Comparator.comparing(CSVObject::getContenedoresAndName).thenComparing(CSVObject::getValores));
		for (CSVObject csvObject : csvObjectList) {
			printerObjetos.printRecord(csvObject.getContenedoresAndName(), csvObject.getValores(), csvObject.getId(), csvObject.getParent());
		}

		printerObjetos.close();
		csvObjetos.close();
	}

	public String calculateContainersString(InventoryObject inventoryObject) {
		if (inventoryObject.getParent() == null || inventoryObject.getParent() == 0) {
			return "";
		} else {
			InventoryObject parentObject = objectList.stream().filter(x -> x.getId().compareTo(inventoryObject.getParent()) == 0).findAny().get();
			if (parentObject.getParent() != null && parentObject.getParent() > 0) {
				return calculateContainersString(parentObject) + ":" + parentObject.getName();
			} else {
				return parentObject.getName();
			}
		}
	}

	public List<InventoryObject> findByName(String name) {
		return objectList.stream().filter(x -> x.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
	}

	public List<InventoryObject> getFilteredByValue(String value) {
		return objectList.stream().filter(x -> x.getPropertyValueList().stream().filter(y -> y.getValue().toLowerCase().contains(value.toLowerCase())).findAny().isPresent()).collect(Collectors.toList());
	}

	public Optional<InventoryObject> findById(Integer idInteger) {
		return objectList.stream().filter(x -> x.getId().compareTo(idInteger) == 0).findAny();
	}

	public List<InventoryObject> findAllByPropertyAndValue(String property, String value) {
		return objectList.stream()
				.filter(object -> object.getPropertyValueList().stream().filter(pv -> pv.getProperty().contentEquals(property) && pv.getValue().contentEquals(value)).findAny().isPresent())
				.collect(Collectors.toList());
	}

	public List<String> findDistinctProperties() {
		return objectList.stream().flatMap(x -> x.getPropertyValueList().stream().map(y -> y.getProperty())).distinct().collect(Collectors.toList());
	}	

	/**
	 * @Query("select distinct v.propiedad from Valor v where v.propiedad like
	 * %:valor% order by v.propiedad")
	 * 
	 * @param valor
	 * @return
	 */
	public List<String> findDistinctPropertiesContaining(String valor) {
		return objectList.stream().flatMap(x -> x.getPropertyValueList().stream().map(y -> y.getProperty())).filter(x -> x.toLowerCase().contains(valor.toLowerCase())).distinct().collect(Collectors.toList());
	}

	/**
	 * @Query("select distinct v.valor from Valor v where v.propiedad = :propiedad
	 * and v.valor like %:valor% order by v.valor")
	 * 
	 * @param propiedad
	 * @param valor
	 * @return
	 */
	public List<String> findDistinctValuesContainingPropertyValue(String propiedad, String valor) {
		return objectList.stream()
			.flatMap(x -> x.getPropertyValueList().stream()
				.filter(y -> y.getProperty().contentEquals(propiedad))
				.map(y -> y.getValue()))
			.filter(x -> x.toLowerCase().contains(valor.toLowerCase()))
			.distinct().collect(Collectors.toList());	
	}

	public List<String> findDistinctValuesContainingProperty(String propiedad) {
		return objectList.stream()
				.flatMap(x -> x.getPropertyValueList().stream()
						.filter(y -> y.getProperty().contentEquals(propiedad))
						.map(y -> y.getValue()))
				.distinct().collect(Collectors.toList());
	}

	public List<InventoryObject> findAllContainingProperty(String property) {
		return objectList.stream()
				.filter(x -> x.getPropertyValueList().stream()
						.filter(y -> y.getProperty().toLowerCase().contains(property.toLowerCase())).findAny()
						.isPresent())
				.collect(Collectors.toList());
	}

	public List<InventoryObject> findAllContainingValue(String value) {
		return objectList.stream()
				.filter(x -> x.getPropertyValueList().stream()
						.filter(y -> y.getValue().toLowerCase().contains(value.toLowerCase())).findAny()
						.isPresent())
				.collect(Collectors.toList());
	}

	/**
	 * 
	 * @param value Sucesión de palabras de filtro separadas por espacios
	 * @return
	 */
	public List<InventoryObject> findAllByFTS(String value) {
		String[] valueArray = value.split(" ");
		List<InventoryObject> result = new ArrayList<>();
		List<InventoryObject> resultTmp = new ArrayList<>();
		for (String v : valueArray) {
			resultTmp = objectList.stream()
					.filter(x -> x.getName().toLowerCase().contains(v.toLowerCase())
							|| x.getPropertyValueList().stream()
									.filter(y -> y.getValue().toLowerCase().contains(v.toLowerCase())).findAny().isPresent())
					.collect(Collectors.toList());
			if (result.isEmpty()) {
				result.addAll(resultTmp);
			} else {
				result.retainAll(resultTmp);
			}
		}
		return result;
	}

	public List<InventoryObject> findAllContainingPropertyAndValue(String property, String value) {
		return objectList.stream()
				.filter(x -> x.getPropertyValueList().stream()
						.filter(y -> y.getProperty().toLowerCase().contains(property.toLowerCase()) 
						          && y.getValue().toLowerCase().contains(value.toLowerCase()))
						.findAny()
						.isPresent())
				.collect(Collectors.toList());
	}	

	public List<InventoryObject> findAllFJObjectByPadreIdOrderByNombreAsc(Integer parentIdInteger) {
		return objectList.stream()
				.filter(x -> x.getParent().compareTo(parentIdInteger) == 0)
				.sorted(Comparator.comparing(InventoryObject::getName))
				.collect(Collectors.toList());
	}

	public void deleteObject(Integer objectId) {
		objectList.removeIf(x -> x.getId().compareTo(objectId) == 0);
	}

	public void saveOrUpdate(InventoryObject objeto) {
		objeto.setContainers(calculateContainersString(objeto));
		if (objeto.getId() == null || objeto.getId() <= 0) {
			objeto.setId(objectList.stream().mapToInt(InventoryObject::getId).max().getAsInt() + 1);
			if (StringUtils.isEmpty(objeto.getName())) {
				objeto.setName(Integer.toString(Integer.parseInt(Integer.valueOf(objeto.getId()).toString(), 10), 36));
			}
			objectList.add(objeto);
		} else {
			if (StringUtils.isEmpty(objeto.getName())) {
				objeto.setName(Integer.toString(Integer.parseInt(Integer.valueOf(objeto.getId()).toString(), 10), 36));
			}
			InventoryObject fjObject = objectList.stream().filter(x -> x.getId().compareTo(objeto.getId()) == 0).findAny().get();
			fjObject.setName(objeto.getName());
			fjObject.setContainers(objeto.getContainers());
			fjObject.setParent(objeto.getParent());
			fjObject.getPropertyValueList().clear();
			fjObject.getPropertyValueList().addAll(objeto.getPropertyValueList());
		}
	}
}
