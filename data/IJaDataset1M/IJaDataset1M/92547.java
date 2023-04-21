package com.indigen.victor.ontology;

import org.w3c.dom.Node;
import com.indigen.victor.actions.VictorAction;
import com.indigen.victor.core.Segment;

public class StringOntoClass extends BuiltinOntoClass {

    public StringOntoClass(VictorAction action, String id) {
        super(action, id);
    }

    public String getValueType() {
        return "inline";
    }

    public void formatData(Node dom, Segment segment) {
        if (segment.getNoLocation() == false) {
            String text = segment.getText(dom);
            if (text != null) segment.setValue(text);
        }
    }
}
