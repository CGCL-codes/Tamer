package org.gwanted.gwt.widget.table.client.model.beans;

/**
 * @author Miguel A. Rager
 */
public class HeaderCell extends Cell {

    public static Scope SCOPE_ROW = new Scope("row");

    public static Scope SCOPE_COL = new Scope("col");

    public static Scope SCOPE_ROWGROUP = new Scope("rowgroup");

    public static Scope SCOPE_COLGROUP = new Scope("colgroup");

    private String axis;

    private String headers;

    private Scope scope;

    public String getAxis() {
        return axis;
    }

    public void setAxis(String axis) {
        this.axis = axis;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public static class Scope extends ConstantString {

        public Scope(String constant) {
            super(constant);
        }
    }
}
