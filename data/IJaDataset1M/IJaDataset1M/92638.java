package org.formaria.editor.project.pages.components;

import java.awt.Component;
import java.util.Hashtable;
import org.formaria.builder.AriaBuilder;
import org.formaria.editor.project.pages.PageResource;
import org.formaria.swing.Page;
import org.formaria.aria.ProjectManager;
import org.formaria.aria.data.DataBinding;

/**
 * A helper for the List types
 * <p> Copyright (c) Formaria Ltd., 2002-2005</p>
 * <p> $Revision: 1.4 $</p>
 */
public class ListHelper extends PropertyHelper {

    public ListHelper(boolean isSwing) {
        className = isSwing ? org.formaria.swing.List.class.toString() : org.formaria.awt.List.class.toString();
        cleanupClassName();
        componentType = Page.LIST;
        properties.add("ActionHandler");
        if (!isSwing) properties.add("ItemHandler"); else properties.add("ListSelectionHandler");
        properties.add("MouseHandler");
        properties.add("Data");
    }
}
