package jbreport.xrl.dom;

import jbreport.xrl.XRLGroup;

/**
 * 
 * @author Grant Finnemore
 * @version $Revision: 1.1 $
 */
public class XRLGroupImpl extends XRLCompositeImpl implements XRLGroup {

    public XRLGroupImpl(XRLDocumentImpl ownerDocument, String value) {
        super(ownerDocument, value);
    }

    public XRLGroupImpl(XRLDocumentImpl ownerDocument, String namespaceURI, String qualifiedName) {
        super(ownerDocument, namespaceURI, qualifiedName);
    }
}
