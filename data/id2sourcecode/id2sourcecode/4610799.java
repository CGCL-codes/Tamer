    @SuppressWarnings("unchecked")
    private byte[] getXmlSignatureDigestValue(DigestAlgo digestAlgo, List<DigestInfo> digestInfos, List<X509Certificate> signingCertificateChain) throws ParserConfigurationException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, MarshalException, javax.xml.crypto.dsig.XMLSignatureException, TransformerFactoryConfigurationError, TransformerException, IOException, SAXException {
        Document document = getEnvelopingDocument();
        if (null == document) {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.newDocument();
        }
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
        XMLSignContext xmlSignContext = new DOMSignContext(key, document);
        URIDereferencer uriDereferencer = getURIDereferencer();
        if (null != uriDereferencer) {
            xmlSignContext.setURIDereferencer(uriDereferencer);
        }
        if (null != this.signatureNamespacePrefix) {
            xmlSignContext.putNamespacePrefix(javax.xml.crypto.dsig.XMLSignature.XMLNS, this.signatureNamespacePrefix);
        }
        XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM", new org.jcp.xml.dsig.internal.dom.XMLDSigRI());
        List<Reference> references = new LinkedList<Reference>();
        addDigestInfosAsReferences(digestInfos, signatureFactory, references);
        String localSignatureId;
        if (null == this.signatureId) {
            localSignatureId = "xmldsig-" + UUID.randomUUID().toString();
        } else {
            localSignatureId = this.signatureId;
        }
        List<XMLObject> objects = new LinkedList<XMLObject>();
        for (SignatureFacet signatureFacet : this.signatureFacets) {
            LOG.debug("invoking signature facet: " + signatureFacet.getClass().getSimpleName());
            signatureFacet.preSign(signatureFactory, document, localSignatureId, signingCertificateChain, references, objects);
        }
        SignatureMethod signatureMethod = signatureFactory.newSignatureMethod(getSignatureMethod(digestAlgo), null);
        CanonicalizationMethod canonicalizationMethod = signatureFactory.newCanonicalizationMethod(getCanonicalizationMethod(), (C14NMethodParameterSpec) null);
        SignedInfo signedInfo = signatureFactory.newSignedInfo(canonicalizationMethod, signatureMethod, references);
        String signatureValueId = localSignatureId + "-signature-value";
        javax.xml.crypto.dsig.XMLSignature xmlSignature = signatureFactory.newXMLSignature(signedInfo, null, objects, localSignatureId, signatureValueId);
        DOMXMLSignature domXmlSignature = (DOMXMLSignature) xmlSignature;
        Node documentNode = document.getDocumentElement();
        if (null == documentNode) {
            documentNode = document;
        }
        domXmlSignature.marshal(documentNode, this.signatureNamespacePrefix, (DOMCryptoContext) xmlSignContext);
        for (XMLObject object : objects) {
            LOG.debug("object java type: " + object.getClass().getName());
            List<XMLStructure> objectContentList = object.getContent();
            for (XMLStructure objectContent : objectContentList) {
                LOG.debug("object content java type: " + objectContent.getClass().getName());
                if (false == objectContent instanceof Manifest) {
                    continue;
                }
                Manifest manifest = (Manifest) objectContent;
                List<Reference> manifestReferences = manifest.getReferences();
                for (Reference manifestReference : manifestReferences) {
                    if (null != manifestReference.getDigestValue()) {
                        continue;
                    }
                    DOMReference manifestDOMReference = (DOMReference) manifestReference;
                    manifestDOMReference.digest(xmlSignContext);
                }
            }
        }
        List<Reference> signedInfoReferences = signedInfo.getReferences();
        for (Reference signedInfoReference : signedInfoReferences) {
            DOMReference domReference = (DOMReference) signedInfoReference;
            if (null != domReference.getDigestValue()) {
                continue;
            }
            domReference.digest(xmlSignContext);
        }
        TemporaryDataStorage temporaryDataStorage = getTemporaryDataStorage();
        OutputStream tempDocumentOutputStream = temporaryDataStorage.getTempOutputStream();
        writeDocument(document, tempDocumentOutputStream);
        temporaryDataStorage.setAttribute(SIGNATURE_ID_ATTRIBUTE, localSignatureId);
        DOMSignedInfo domSignedInfo = (DOMSignedInfo) signedInfo;
        ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
        domSignedInfo.canonicalize(xmlSignContext, dataStream);
        byte[] octets = dataStream.toByteArray();
        MessageDigest jcaMessageDigest = MessageDigest.getInstance(digestAlgo.getAlgoId());
        byte[] digestValue = jcaMessageDigest.digest(octets);
        return digestValue;
    }
