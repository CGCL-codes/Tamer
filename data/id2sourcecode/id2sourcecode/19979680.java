    @Test
    public void testSignedOOXMLOffice2010ValidOOXML() throws Exception {
        URL url = OOXMLSignatureVerifierTest.class.getResource("/hallo.docx");
        OOXMLSignatureVerifier verifier = new OOXMLSignatureVerifier();
        List<X509Certificate> result = verifier.getSigners(url);
        assertNotNull(result);
        assertEquals(1, result.size());
        X509Certificate signer = result.get(0);
        LOG.debug("signer: " + signer.getSubjectX500Principal());
        byte[] document = IOUtils.toByteArray(url.openStream());
        List<String> signatureResourceNames = verifier.getSignatureResourceNames(document);
        Document signatureDocument = verifier.getSignatureDocument(new ByteArrayInputStream(document), signatureResourceNames.get(0));
        NodeList signatureNodeList = signatureDocument.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
        Element signatureElement = (Element) signatureNodeList.item(0);
        KeyInfoKeySelector keySelector = new KeyInfoKeySelector();
        DOMValidateContext domValidateContext = new DOMValidateContext(keySelector, signatureElement);
        domValidateContext.setProperty("org.jcp.xml.dsig.validateManifests", Boolean.TRUE);
        OOXMLURIDereferencer dereferencer = new OOXMLURIDereferencer(document);
        domValidateContext.setURIDereferencer(dereferencer);
        XMLSignatureFactory xmlSignatureFactory = XMLSignatureFactory.getInstance();
        XMLSignature xmlSignature = xmlSignatureFactory.unmarshalXMLSignature(domValidateContext);
        assertTrue(verifier.isValidOOXMLSignature(xmlSignature, document));
    }
