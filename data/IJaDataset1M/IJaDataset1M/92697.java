package com.teracode.prototipogwt.frontend.client.visualcomponents;

import java.util.Map;
import com.google.gwt.dom.client.Element;

public interface Component {

    public void setParameters(Map<String, String> params);

    public Element getElement();
}
