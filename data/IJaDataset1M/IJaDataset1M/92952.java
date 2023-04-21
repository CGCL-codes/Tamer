package serene.restrictor;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import serene.validation.schema.simplified.components.SNameClass;
import serene.validation.schema.simplified.components.SPattern;
import serene.validation.schema.simplified.components.SChoicePattern;
import serene.validation.schema.simplified.components.SInterleave;
import serene.validation.schema.simplified.components.SMixed;
import serene.validation.schema.simplified.components.SGroup;
import serene.validation.handlers.error.ErrorDispatcher;

class ElementLimitationNamingController extends LimitationNamingController {

    ElementLimitationNamingController(ControllerPool pool, ErrorDispatcher errorDispatcher) {
        super(pool, errorDispatcher);
    }

    public void recycle() {
        nameClasses.clear();
        namedPatterns.clear();
        compositors.clear();
        compositorPatterns.clear();
        compositorPath.clear();
        compositorPatternPath.clear();
        compositorRelevance.clear();
        cardinalityRelevance.clear();
        pool.recycle(this);
    }

    void reportError(SPattern context, int i, int j) throws SAXException {
        SPattern e1 = namedPatterns.get(i);
        SPattern e2 = namedPatterns.get(j);
        String message = "Unsupported schema configuration. " + "For the moment serene does not support overlapping name classes in elements in the context of a <group> that has multiple cardinality and is in the context of an <interleave>:" + "\n<" + e1.getQName() + "> at " + e1.getLocation(restrictToFileName) + "\n<" + e2.getQName() + "> at " + e2.getLocation(restrictToFileName) + ".";
        errorDispatcher.error(new SAXParseException(message, null));
    }

    public String toString() {
        return "ElementNamingController";
    }
}
