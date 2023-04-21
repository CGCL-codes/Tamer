package com.volantis.mcs.dissection.dom;

import com.volantis.mcs.dissection.DissectableAreaAttributes;
import com.volantis.mcs.dissection.DissectableAreaIdentity;
import com.volantis.mcs.dissection.KeepTogetherAttributes;
import com.volantis.mcs.dissection.links.ShardLinkAction;
import com.volantis.mcs.dissection.links.ShardLinkAttributes;
import com.volantis.mcs.dissection.links.ShardLinkConditionalAttributes;
import com.volantis.mcs.dissection.links.ShardLinkGroupAttributes;
import com.volantis.mcs.dissection.links.rules.ShardLinkContentRule;
import com.volantis.mcs.dissection.links.rules.StandardContentRules;
import com.volantis.mcs.dom.NodeAnnotation;
import com.volantis.xml.sax.AnnotatedAttributesImpl;
import com.volantis.synergetics.cornerstone.utilities.WhitespaceUtilities;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class processes a SAX stream into a DissectableDocument.
 * <p>
 * It treats the internal DTD if any as a string table that defines common
 * strings and entity references are references into that string table.
 * <p>
 * It recognizes the following special elements.
 * <dl>
 *   <dt>DISSECTABLE-AREA
 *   <dd>Defines a dissectable area
 *   </dd>
 * todo: Document the rest of this stuff
 * </dl>
 * 
 * NOTE: The way this class works is non-obvious. It is a delegating content
 * handler, which either delegates to the builder or sometimes to a null
 * content handler. It also sometimes accesses the builder directly, for api
 * calls which are outside the normal SAX content handler set. When it does
 * access the builder directly, you need to make sure that you respect the
 * way that the parent delegating handler works, e.g. you need to call enter()
 * and exit() at the correct points. I think given that this may be clearer
 * if it avoided uses the builder via two separate interfaces - but that would
 * mean either getting rid of the delgating content handler, or adding more
 * methods on to it...
 */
public class DissectableContentHandler extends DelegatingContentHandler implements DissectableTestConstants, LexicalHandler, DeclHandler {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private static String SPECIAL_NAMESPACE_URI = "http://test.volantis.com/dissection";

    public static String COMMON_NAMESPACE_URI = "http://test.volantis.com/common";

    public static String COMMON_ELEMENT_PREFIX = "element.";

    private static String SHARD_LINK_URL_PARAMETER = "[url]";

    private DissectableDocumentBuilder builder;

    private NullContentHandler nullContentHandler;

    private AnnotatedAttributesImpl extendedAttributes;

    private TestDocumentDetails testDocumentDetails;

    private boolean insideDTD;

    public DissectableContentHandler() {
        nullContentHandler = new NullContentHandler();
        extendedAttributes = new AnnotatedAttributesImpl();
        testDocumentDetails = new TestDocumentDetails();
    }

    public DissectableContentHandler(DissectableDocumentBuilder builder) {
        this();
        initialise(builder);
    }

    public void initialise(DissectableDocumentBuilder builder) {
        this.builder = builder;
        pushContentHandler(builder);
        testDocumentDetails.initialise();
    }

    public TestDocumentDetails getTestDocumentDetails() throws SAXException {
        testDocumentDetails.setDocument(builder.getDissectableDocument());
        return testDocumentDetails;
    }

    private DissectableAreaIdentity getDissectableAreaIdentity(Attributes attributes) {
        String name = attributes.getValue("name");
        String inclusionPath = attributes.getValue("inclusionPath");
        return new DissectableAreaIdentity(inclusionPath, name);
    }

    private void startSpecial(String localName, Attributes attributes) throws SAXException {
        if (localName.equals(SPLIT_STRING)) {
        } else {
            NodeAnnotation annotation;
            ElementType type;
            if (localName.equals(DISSECTABLE_AREA)) {
                DissectableAreaAttributes attrs = new DissectableAreaAttributes();
                DissectableAreaIdentity identity = getDissectableAreaIdentity(attributes);
                attrs.setIdentity(identity);
                annotation = attrs;
                type = DissectionElementTypes.getDissectableAreaType();
            } else if (localName.equals(KEEP_TOGETHER)) {
                KeepTogetherAttributes attrs = new KeepTogetherAttributes();
                String after = attributes.getValue("forceBreakAfter");
                String before = attributes.getValue("forceBreakBefore");
                attrs.setForceBreakAfter("true".equalsIgnoreCase(after));
                attrs.setForceBreakBefore("true".equalsIgnoreCase(before));
                annotation = attrs;
                type = DissectionElementTypes.getKeepTogetherType();
            } else if (localName.equals(SHARD_LINK)) {
                ShardLinkAttributes attrs = new ShardLinkAttributes();
                String value = attributes.getValue("action");
                ShardLinkAction action;
                if (value.equals("next")) {
                    action = ShardLinkAction.NEXT;
                } else if (value.equals("previous")) {
                    action = ShardLinkAction.PREVIOUS;
                } else {
                    throw new IllegalArgumentException("Unknown action: " + value);
                }
                attrs.setAction(action);
                attrs.setUserData(attributes.getValue("userData"));
                annotation = attrs;
                type = DissectionElementTypes.getShardLinkType();
            } else if (localName.equals(SHARD_LINK_GROUP)) {
                DissectableAreaIdentity identity = getDissectableAreaIdentity(attributes);
                ShardLinkGroupAttributes attrs = new ShardLinkGroupAttributes();
                attrs.setDissectableArea(identity);
                annotation = attrs;
                type = DissectionElementTypes.getShardLinkGroupType();
            } else if (localName.equals(SHARD_LINK_CONDITIONAL)) {
                ShardLinkConditionalAttributes attrs = new ShardLinkConditionalAttributes();
                ShardLinkContentRule rule;
                String value = attributes.getValue("rule");
                if (value.equals("any")) {
                    rule = StandardContentRules.getAnyRule();
                } else if (value.equals("separator")) {
                    rule = StandardContentRules.getSeparatorRule();
                } else {
                    throw new IllegalArgumentException("Unknown rule: " + value);
                }
                attrs.setContentRule(rule);
                annotation = attrs;
                type = DissectionElementTypes.getShardLinkConditionalType();
            } else {
                throw new IllegalArgumentException("Unknown element: " + localName);
            }
            builder.startSpecialElement(type, annotation);
        }
    }

    private void endSpecial(String localName) throws SAXException {
        if (localName.equals(SPLIT_STRING)) {
        } else {
            builder.endSpecialElement();
        }
    }

    private void startCommonString(String name) throws SAXException {
        int index = testDocumentDetails.getCommonStringIndex(name);
        if (index != -1) {
            pushContentHandler(nullContentHandler);
        }
    }

    private void endCommonString(String name) throws SAXException {
        int index = testDocumentDetails.getCommonStringIndex(name);
        if (index != -1) {
            popContentHandler();
            builder.addSharedStringReference(index);
        }
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException {
        if (SPECIAL_NAMESPACE_URI.equals(namespaceURI)) {
            startSpecial(localName, attributes);
            return;
        }
        extendedAttributes.setAttributes(attributes);
        int count = attributes.getLength();
        for (int i = 0; i < count; i += 1) {
            String attrNamespaceURI = attributes.getURI(i);
            String attrLocalName = attributes.getLocalName(i);
            if (!SPECIAL_NAMESPACE_URI.equals(attrNamespaceURI)) {
                continue;
            }
            String value = attributes.getValue(i);
            Object annotation = checkSpecialAttribute(attrLocalName, value);
            if (annotation == null) {
                throw new IllegalStateException("No object returned for special attribute");
            }
            extendedAttributes.setAnnotation(i, annotation);
        }
        if (COMMON_NAMESPACE_URI.equals(namespaceURI)) {
            enter();
            String entity = COMMON_ELEMENT_PREFIX + localName;
            int index = testDocumentDetails.getCommonStringIndex(entity);
            if (index == -1) {
                throw new IllegalStateException("Common element '" + localName + "' does not have matching entity '" + entity + "'");
            }
            builder.startCommonElement(index, extendedAttributes);
            return;
        }
        super.startElement(namespaceURI, localName, qName, extendedAttributes);
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if (SPECIAL_NAMESPACE_URI.equals(namespaceURI)) {
            endSpecial(localName);
            return;
        }
        super.endElement(namespaceURI, localName, qName);
    }

    /**
     * Examine the specified attribute to see whether it is a special
     * shard link parameter. If it is then return a special object that
     * represents it.
     */
    protected Object checkSpecialAttribute(String localName, String value) {
        if (value.equals(SHARD_LINK_URL_PARAMETER)) {
            return builder.getShardLinkURLParameter();
        } else {
            throw new IllegalStateException("Unknown shard link parameter: " + value);
        }
    }

    public void characters(char[] chars, int offset, int length) throws SAXException {
        if (WhitespaceUtilities.isWhitespace(chars, offset, length)) {
            return;
        }
        super.characters(chars, offset, length);
    }

    public void comment(char[] chars, int i, int i1) throws SAXException {
    }

    public void endCDATA() throws SAXException {
    }

    public void endDTD() throws SAXException {
    }

    public void endEntity(String name) throws SAXException {
        if (name.equals("[dtd]")) {
            if (!insideDTD) {
                throw new InternalError("Expecting to be inside DTD");
            }
            insideDTD = false;
        } else if (!insideDTD && !name.startsWith("%")) {
            endCommonString(name);
        }
    }

    public void startCDATA() throws SAXException {
    }

    public void startDTD(String s, String s1, String s2) throws SAXException {
    }

    public void startEntity(String name) throws SAXException {
        if (name.equals("[dtd]")) {
            if (insideDTD) {
                throw new InternalError("Not expecting nested dtd entities");
            }
            insideDTD = true;
        } else if (!insideDTD && !name.startsWith("%")) {
            startCommonString(name);
        }
    }

    public void attributeDecl(String s, String s1, String s2, String s3, String s4) throws SAXException {
    }

    public void elementDecl(String s, String s1) throws SAXException {
    }

    public void externalEntityDecl(String s, String s1, String s2) throws SAXException {
    }

    public void internalEntityDecl(String name, String value) throws SAXException {
        if (!insideDTD && !name.startsWith("%")) {
            int index = builder.addStringTableEntry(value);
            testDocumentDetails.addCommonString(name, value, index);
        }
    }

    private static class NullContentHandler extends DefaultHandler {
    }
}
