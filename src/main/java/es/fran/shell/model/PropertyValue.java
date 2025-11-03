package es.fran.shell.model;

import java.io.Serializable;
import java.text.Normalizer;

public class PropertyValue implements Serializable {

	private static final long serialVersionUID = -2853057396650740156L;
	
	private String property;
	private String value;

	public String getProperty() {
		return property;
	}

	public void setProperty(String propiedad) {
		this.property = propiedad.replaceAll(" ", "-").replaceAll(":", "_");
	}

	public String getValue() {
		return value;
	}

	public void setValue(String valor) {
		if (valor != null) {
			this.value = Normalizer.normalize(valor.trim(), Normalizer.Form.NFD);
		} else {
			this.value = valor;
		}
	}

	public PropertyValue(String propiedad, String valor) {
		this.property = propiedad;
		if (valor != null) {
			setValue(valor);
		}
	}

	@Override
	public int hashCode() {
		return property.hashCode() + ":".hashCode() + value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PropertyValue) {
			PropertyValue pv = (PropertyValue) obj;
			return property.equals(pv.getProperty()) && value.equals(pv.getValue());
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		if (value == null) {
			return property;
		}
		return property + ":" + value.replaceAll(":", "_");
	}
}
