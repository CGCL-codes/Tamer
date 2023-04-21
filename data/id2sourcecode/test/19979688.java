    @Test
    public void testVerifySignature() throws Exception {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger("org.jcp.xml.dsig.internal.dom");
        logger.log(Level.FINE, "test");
        URL url = OOXMLSignatureVerifierTest.class.getResource("/hello-world-signed.docx");
        String signatureResourceName = getSignatureResourceName(url);
        LOG.debug("signature resource name: " + signatureResourceName);
        OOXMLProvider.install();
        ZipInputStream zipInputStream = new ZipInputStream(url.openStream());
        ZipEntry zipEntry;
        while (null != (zipEntry = zipInputStream.getNextEntry())) {
            if (false == signatureResourceName.equals(zipEntry.getName())) {
                continue;
            }
            Document signatureDocument = loadDocument(zipInputStream);
            LOG.debug("signature loaded");
            NodeList signatureNodeList = signatureDocument.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
            assertEquals(1, signatureNodeList.getLength());
            Node signatureNode = signatureNodeList.item(0);
            KeyInfoKeySelector keySelector = new KeyInfoKeySelector();
            DOMValidateContext domValidateContext = new DOMValidateContext(keySelector, signatureNode);
            domValidateContext.setProperty("org.jcp.xml.dsig.validateManifests", Boolean.TRUE);
            OOXMLURIDereferencer dereferencer = new OOXMLURIDereferencer(url);
            domValidateContext.setURIDereferencer(dereferencer);
            XMLSignatureFactory xmlSignatureFactory = XMLSignatureFactory.getInstance();
            XMLSignature xmlSignature = xmlSignatureFactory.unmarshalXMLSignature(domValidateContext);
            boolean validity = xmlSignature.validate(domValidateContext);
            assertTrue(validity);
            List<?> objects = xmlSignature.getObjects();
            for (Object object : objects) {
                LOG.debug("ds:Object class type: " + object.getClass().getName());
            }
            break;
        }
    }
