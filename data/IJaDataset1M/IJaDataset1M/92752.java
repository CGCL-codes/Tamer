package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.html.component.Component;
import org.nakedobjects.plugins.html.image.ImageLookup;
import org.nakedobjects.plugins.html.request.Request;
import org.nakedobjects.runtime.context.NakedObjectsContext;

public class CollectionIcon implements Component {

    private final NakedObject collection;

    private final String id;

    private final String description;

    public CollectionIcon(final NakedObject element, final String description, final String id) {
        this.collection = element;
        this.description = description;
        this.id = id;
    }

    public void write(final PrintWriter writer) {
        final TypeOfFacet facet = collection.getSpecification().getFacet(TypeOfFacet.class);
        final Class<?> elementType = facet.value();
        final NakedObjectSpecification elementSpecification = NakedObjectsContext.getSpecificationLoader().loadSpecification(elementType);
        writer.print("<div class=\"item\">");
        writer.print("<a href=\"");
        writer.print(Request.COLLECTION_COMMAND + ".app?id=");
        writer.print(id);
        writer.print("\"");
        if (description != null) {
            writer.print(" title=\"");
            writer.print(description);
            writer.print("\"");
        }
        writer.print("><img src=\"");
        writer.print(ImageLookup.image(elementSpecification));
        writer.print("\" alt=\"");
        final String singularName = elementSpecification.getSingularName();
        writer.print(singularName);
        writer.print(" collection\" />");
        writer.print(collection.titleString());
        writer.print("</a>");
        writer.println("</div>");
    }
}
