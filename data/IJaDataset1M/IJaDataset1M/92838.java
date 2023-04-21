package com.rapidminer.parameter;

import org.w3c.dom.Element;
import com.rapidminer.tools.XMLException;

/**
 * A parameter type for boolean parameters. Operators ask for the boolean value
 * with {@link com.rapidminer.operator.Operator#getParameterAsBoolean(String)}.
 * 
 * @author Ingo Mierswa, Simon Fischer
 */
public class ParameterTypeBoolean extends ParameterTypeSingle {

    private static final long serialVersionUID = 6524969076774489545L;

    private static final String ATTRIBUTE_DEFAULT = "default";

    private boolean defaultValue = false;

    public ParameterTypeBoolean(Element element) throws XMLException {
        super(element);
        this.defaultValue = Boolean.valueOf(element.getAttribute(ATTRIBUTE_DEFAULT));
    }

    public ParameterTypeBoolean(String key, String description, boolean defaultValue, boolean expert) {
        this(key, description, defaultValue);
        setExpert(expert);
    }

    public ParameterTypeBoolean(String key, String description, boolean defaultValue) {
        super(key, description);
        this.defaultValue = defaultValue;
    }

    public boolean getDefault() {
        return defaultValue;
    }

    @Override
    public Object getDefaultValue() {
        return Boolean.valueOf(defaultValue);
    }

    @Override
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = (Boolean) defaultValue;
    }

    /** Returns false. */
    @Override
    public boolean isNumerical() {
        return false;
    }

    @Override
    public String getRange() {
        return "boolean; default: " + defaultValue;
    }

    @Override
    protected void writeDefinitionToXML(Element typeElement) {
        typeElement.setAttribute(ATTRIBUTE_DEFAULT, defaultValue + "");
    }
}
