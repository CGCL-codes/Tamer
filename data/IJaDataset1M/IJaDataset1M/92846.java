package org.vardb.util.mongo.resources.dao;

@SuppressWarnings("serial")
public class CFamily extends CResource {

    public enum FamilyAttribute {

        pathogen, ortholog
    }

    ;

    public CFamily() {
    }

    public CFamily(String identifier) {
        super(identifier);
    }

    public void setPathogen(String pathogen) {
        setAttribute(FamilyAttribute.pathogen, pathogen);
    }

    public String getPathogen() {
        return (String) getAttribute(FamilyAttribute.pathogen);
    }

    public void setOrtholog(String ortholog) {
        setAttribute(FamilyAttribute.ortholog, ortholog);
    }

    public String getOrtholog() {
        return (String) getAttribute(FamilyAttribute.ortholog);
    }
}
