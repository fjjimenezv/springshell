package es.fran.shell.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

public class InventoryObject {
    private Integer id;
    private Integer parent;
    private String name;
    private String containers;
    private String image;
    private String pdf;
    private List<PropertyValue> propertyValueList;

    public InventoryObject() {
        this.propertyValueList = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getParent() {
        return parent;
    }
    public void setParent(Integer parent) {
        this.parent = parent;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PropertyValue> getPropertyValueList() {
        return propertyValueList;
    }

    public void setPropertyValueList(List<PropertyValue> propiedadValorList) {
        this.propertyValueList = propiedadValorList;
    }

    public String getContainers() {
        return containers;
    }

    public void setContainers(String contenedores) {
        this.containers = contenedores;
    }

    public List<PropertyValue> getMetadataWithLabel() {
        if (StringUtils.isNotEmpty(name)) {
            List<PropertyValue> tmpPropertyValueList = new ArrayList<>(propertyValueList);
            tmpPropertyValueList.add(new PropertyValue("label", name));
            return tmpPropertyValueList;
        } else {
            return propertyValueList;
        }
    }

    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        for (PropertyValue propiedadValor : propertyValueList) {
            if (!sb.isEmpty()) {
                sb.append("; ");
            }
            sb.append(propiedadValor.toString());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        String strTmp = "";
        if (CollectionUtils.isNotEmpty(propertyValueList)) {            
            for (PropertyValue v : propertyValueList) {
                if (strTmp.length() == 0) {
                    strTmp = v.getProperty() + ":" + v.getValue();
                } else {
                    strTmp = strTmp + ", " + v.getProperty() + ":" + v.getValue();
                }
            }
        }
        return "id:" + id + ", parent:" + parent + ", label:" + name + ", " + strTmp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }
}
