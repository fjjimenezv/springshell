package es.fran.shell.model;

public class CSVObject {
    private Integer id;
    private Integer parent;
    private String contenedoresAndName;
    private String valores;

    public CSVObject() {
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

    public String getContenedoresAndName() {
        return contenedoresAndName;
    }

    public void setContenedoresAndName(String contenedoresAndName) {
        this.contenedoresAndName = contenedoresAndName;
    }

    public String getValores() {
        return valores;
    }

    public void setValores(String valores) {
        this.valores = valores;
    }


}
