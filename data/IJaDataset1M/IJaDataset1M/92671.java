package net.sf.wgfa.beans;

import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class PropertyBean extends ResourceBean {

    private java.util.List<EntityIdBean> superProperties;

    private java.util.List<EntityIdBean> equivalentProperties;

    private java.util.List<EntityIdBean> domain;

    private boolean isFunctionalProperty;

    public PropertyBean(OntProperty property) {
        super(property);
        superProperties = fillResourceList(property.listSuperProperties());
        equivalentProperties = fillResourceList(property.listEquivalentProperties());
        domain = fillResourceList(property.listDomain());
        isFunctionalProperty = property.isFunctionalProperty();
    }

    public java.util.List<EntityIdBean> getSuperProperties() {
        return superProperties;
    }

    public void setSuperProperties(java.util.List<EntityIdBean> superProperties) {
        this.superProperties = superProperties;
    }

    public java.util.List<EntityIdBean> getEquivalentProperties() {
        return equivalentProperties;
    }

    public void setEquivalentProperties(java.util.List<EntityIdBean> equivalentProperties) {
        this.equivalentProperties = equivalentProperties;
    }

    public java.util.List<EntityIdBean> getDomain() {
        return domain;
    }

    public void setDomain(java.util.List<EntityIdBean> domain) {
        this.domain = domain;
    }

    public boolean isIsFunctionalProperty() {
        return isFunctionalProperty;
    }

    public void setIsFunctionalProperty(boolean isFunctionalProperty) {
        this.isFunctionalProperty = isFunctionalProperty;
    }
}
