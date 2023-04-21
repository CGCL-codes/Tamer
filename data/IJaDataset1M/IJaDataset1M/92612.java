package tafat.metamodel.entity;

import java.text.ParseException;

public abstract class ElectricalTool extends PowerConsumer {

    public void loadAttribute(String name, String value) throws ParseException {
        super.loadAttribute(name, value);
    }

    public void setDefaultValues() throws ParseException {
        super.setDefaultValues();
    }
}
