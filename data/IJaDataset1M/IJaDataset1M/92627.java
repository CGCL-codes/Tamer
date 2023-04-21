package org.librebiz.pureport.definition;

import java.util.ArrayList;
import java.util.List;

public class MacroDefinition extends SectionContainer {

    private List<String> arguments = new ArrayList<String>();

    public String[] getArguments() {
        return arguments.toArray(new String[arguments.size()]);
    }

    public void addArgument(String name) {
        arguments.add(name);
    }

    public boolean hasArgument(String name) {
        return arguments.contains(name);
    }
}
