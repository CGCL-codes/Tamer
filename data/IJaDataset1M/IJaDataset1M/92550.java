package com.quote.web.actionform;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class LookupForm extends ActionForm {

    private String symbol = null;

    public String getSymbol() {
        return (symbol);
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.symbol = null;
    }
}
