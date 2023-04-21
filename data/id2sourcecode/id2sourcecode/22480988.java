    @Test
    public void testJsr105ReferenceUri() throws Exception {
        String uri = FilenameUtils.getName(new File("foo bar.txt").toURI().toURL().getFile());
        KeyPair keyPair = generateKeyPair();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM", new org.jcp.xml.dsig.internal.dom.XMLDSigRI());
        XMLSignContext signContext = new DOMSignContext(keyPair.getPrivate(), document);
        byte[] externalDocument = "hello world".getBytes();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        messageDigest.update(externalDocument);
        byte[] documentDigestValue = messageDigest.digest();
        DigestMethod digestMethod = signatureFactory.newDigestMethod(DigestMethod.SHA1, null);
        Reference reference = signatureFactory.newReference(uri, digestMethod, null, null, null, documentDigestValue);
        SignatureMethod signatureMethod = signatureFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null);
        CanonicalizationMethod canonicalizationMethod = signatureFactory.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE_WITH_COMMENTS, (C14NMethodParameterSpec) null);
        javax.xml.crypto.dsig.SignedInfo signedInfo = signatureFactory.newSignedInfo(canonicalizationMethod, signatureMethod, Collections.singletonList(reference));
        javax.xml.crypto.dsig.XMLSignature xmlSignature = signatureFactory.newXMLSignature(signedInfo, null);
        xmlSignature.sign(signContext);
    }
