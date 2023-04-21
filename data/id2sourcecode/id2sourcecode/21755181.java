    private static XMLSignature signInternal(Node a_node, IMyPrivateKey a_privateKey) throws XMLParseException {
        byte[] digestValue;
        byte[] signatureValue;
        Element elementToSign;
        XMLSignature xmlSignature;
        Element oldSignatureNode;
        if (a_node == null || a_privateKey == null) {
            return null;
        } else if (a_node instanceof Document) {
            elementToSign = ((Document) a_node).getDocumentElement();
        } else if (a_node instanceof Element) {
            elementToSign = (Element) a_node;
        } else {
            return null;
        }
        xmlSignature = new XMLSignature();
        oldSignatureNode = removeSignatureFromInternal(elementToSign);
        try {
            try {
                digestValue = MessageDigest.getInstance("SHA-1").digest(toCanonical(elementToSign));
            } catch (NoSuchAlgorithmException a_e) {
                return null;
            }
            xmlSignature.m_referenceURI = "";
            xmlSignature.m_digestMethod = DIGEST_METHOD_ALGORITHM;
            xmlSignature.m_digestValue = new String(Base64.encode(digestValue, false));
            Document doc = elementToSign.getOwnerDocument();
            Element signedInfoNode = doc.createElement(ELEM_SIGNED_INFO);
            Element canonicalizationNode = doc.createElement(ELEM_CANONICALIZATION_METHOD);
            Element signatureMethodNode = doc.createElement(ELEM_SIGNATURE_METHOD);
            String signatureMethod = a_privateKey.getSignatureAlgorithm().getXMLSignatureAlgorithmReference();
            if (signatureMethod != null) {
                xmlSignature.m_signatureMethod = signatureMethod;
                XMLUtil.setAttribute(signatureMethodNode, ATTR_ALGORITHM, signatureMethod);
            } else {
                xmlSignature.m_signatureMethod = "";
            }
            Element referenceNode = doc.createElement(ELEM_REFERENCE);
            if (xmlSignature.getReferenceURI().length() > 0) {
                referenceNode.setAttribute(ATTR_URI, xmlSignature.getReferenceURI());
            }
            Element digestMethodNode = doc.createElement(ELEM_DIGEST_METHOD);
            XMLUtil.setAttribute(digestMethodNode, ATTR_ALGORITHM, DIGEST_METHOD_ALGORITHM);
            Element digestValueNode = doc.createElement(ELEM_DIGEST_VALUE);
            XMLUtil.setValue(digestValueNode, xmlSignature.m_digestValue);
            referenceNode.appendChild(digestMethodNode);
            referenceNode.appendChild(digestValueNode);
            signedInfoNode.appendChild(canonicalizationNode);
            signedInfoNode.appendChild(signatureMethodNode);
            signedInfoNode.appendChild(referenceNode);
            xmlSignature.m_signedInfoCanonical = toCanonical(signedInfoNode);
            signatureValue = ByteSignature.sign(xmlSignature.m_signedInfoCanonical, a_privateKey);
            signatureValue = a_privateKey.getSignatureAlgorithm().encodeForXMLSignature(signatureValue);
            if (signatureValue == null) {
                return null;
            }
            xmlSignature.m_signatureValue = new String(Base64.encode(signatureValue, false));
            Element signatureValueNode = doc.createElement(ELEM_SIGNATURE_VALUE);
            signatureValueNode.appendChild(doc.createTextNode(xmlSignature.m_signatureValue));
            Element signatureNode = doc.createElement(XML_ELEMENT_NAME);
            signatureNode.appendChild(signedInfoNode);
            signatureNode.appendChild(signatureValueNode);
            elementToSign.appendChild(signatureNode);
            xmlSignature.m_elemSignature = signatureNode;
            xmlSignature.m_bVerified = true;
            return xmlSignature;
        } catch (XMLParseException a_e) {
            if (oldSignatureNode != null) {
                elementToSign.appendChild(oldSignatureNode);
            }
            throw a_e;
        } catch (Exception a_e) {
            LogHolder.log(LogLevel.EXCEPTION, LogType.CRYPTO, "Could not sign XML document!", a_e);
            if (oldSignatureNode != null) {
                elementToSign.appendChild(oldSignatureNode);
            }
            return null;
        }
    }
