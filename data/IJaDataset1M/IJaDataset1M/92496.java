package uk.ac.ebi.intact.util.sanityChecker.model;

/**
 * TODO comment it.
 *
 * @author Catherine Leroy (cleroy@ebi.ac.uk)
 * @version $Id: ControlledvocabBean.java 4358 2005-10-13 10:53:35Z catherineleroy $
 */
public class ControlledvocabBean extends AnnotatedBean {

    private String objclass;

    public ControlledvocabBean() {
    }

    public String getObjclass() {
        return objclass;
    }

    public void setObjclass(String objclass) {
        this.objclass = objclass;
    }
}
