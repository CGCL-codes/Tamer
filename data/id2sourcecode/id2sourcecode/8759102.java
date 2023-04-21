    private byte[] getXmlSignatureDigestValue(String digestAlgo, List<DigestInfo> digestInfos, HttpSession httpSession) throws ParserConfigurationException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, MarshalException, javax.xml.crypto.dsig.XMLSignatureException, TransformerFactoryConfigurationError, TransformerException, MalformedURLException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM", new org.jcp.xml.dsig.internal.dom.XMLDSigRI());
        Key key = new Key() {

            private static final long serialVersionUID = 1L;

            public String getAlgorithm() {
                return null;
            }

            public byte[] getEncoded() {
                return null;
            }

            public String getFormat() {
                return null;
            }
        };
        XMLSignContext signContext = new DOMSignContext(key, document);
        signContext.putNamespacePrefix(javax.xml.crypto.dsig.XMLSignature.XMLNS, "ds");
        List<Reference> references = new LinkedList<Reference>();
        for (DigestInfo digestInfo : digestInfos) {
            byte[] documentDigestValue = digestInfo.digestValue;
            DigestMethod digestMethod = signatureFactory.newDigestMethod(getXmlDigestAlgo(digestInfo.digestAlgo), null);
            String uri = FilenameUtils.getName(new File(digestInfo.description).toURI().toURL().getFile());
            Reference reference = signatureFactory.newReference(uri, digestMethod, null, null, null, documentDigestValue);
            references.add(reference);
        }
        SignatureMethod signatureMethod = signatureFactory.newSignatureMethod(getSignatureMethod(digestAlgo), null);
        CanonicalizationMethod canonicalizationMethod = signatureFactory.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE_WITH_COMMENTS, (C14NMethodParameterSpec) null);
        javax.xml.crypto.dsig.SignedInfo signedInfo = signatureFactory.newSignedInfo(canonicalizationMethod, signatureMethod, references);
        javax.xml.crypto.dsig.XMLSignature xmlSignature = signatureFactory.newXMLSignature(signedInfo, null);
        DOMXMLSignature domXmlSignature = (DOMXMLSignature) xmlSignature;
        domXmlSignature.marshal(document, "ds", (DOMCryptoContext) signContext);
        Source source = new DOMSource(document);
        StringWriter stringWriter = new StringWriter();
        Result result = new StreamResult(stringWriter);
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        xformer.transform(source, result);
        String documentStr = stringWriter.getBuffer().toString();
        httpSession.setAttribute("xmlDocument", documentStr);
        DOMSignedInfo domSignedInfo = (DOMSignedInfo) signedInfo;
        ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
        domSignedInfo.canonicalize(signContext, dataStream);
        byte[] octets = dataStream.toByteArray();
        MessageDigest jcaMessageDigest = MessageDigest.getInstance(digestAlgo);
        byte[] digestValue = jcaMessageDigest.digest(octets);
        return digestValue;
    }
