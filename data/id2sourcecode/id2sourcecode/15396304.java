    private void SignDocumentAllSignableItems(PrivateKey pSigningPrivateKey, X509Certificate pSigningCertificate) throws OpenXML4JException {
        if (this._container.getPackageAccess() != PackageAccess.READ_WRITE) {
            throw new OpenXML4JException("To sign a document package must be open with read write package access");
        }
        ByteArrayOutputStream boutTemp = new ByteArrayOutputStream();
        try {
            _container.save(boutTemp);
        } catch (IOException e) {
            throw new OpenXML4JException("Error saving pre-formatted data to output", e);
        }
        ByteArrayInputStream binTemp = new ByteArrayInputStream(boutTemp.toByteArray());
        try {
            _container = Package.open(binTemp, PackageAccess.READ_WRITE);
        } catch (InvalidFormatException e) {
            throw new OpenXML4JException("Pre-formatted data is not in valid openxml package format", e);
        } catch (IOException e) {
            throw new OpenXML4JException("Error opening pre-formatted data", e);
        }
        ensureOriginPart();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        org.w3c.dom.Document doc;
        try {
            doc = dbf.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new OpenXML4JException("Document parsing error", e);
        }
        final XMLSignatureFactory fac = OPCSignatureHelper.CreateXMLSignatureFactory();
        List<Reference> signedInfoReferences = new Vector<Reference>();
        Reference refIdPackageObject;
        Reference refIdOfficeObject;
        try {
            refIdPackageObject = fac.newReference("#" + PackageDigitalSignature.PackageObjectIdentifier, fac.newDigestMethod(DigestMethod.SHA1, null), null, "http://www.w3.org/2000/09/xmldsig#Object", null);
            refIdOfficeObject = fac.newReference("#" + PackageDigitalSignature.OfficeObjectIdentifier, fac.newDigestMethod(DigestMethod.SHA1, null), null, "http://www.w3.org/2000/09/xmldsig#Object", null);
        } catch (NoSuchAlgorithmException e) {
            throw new OpenXML4JException("XML signing algorithm error", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new OpenXML4JException("XML signing algorithm parameters error", e);
        }
        signedInfoReferences.add(refIdPackageObject);
        signedInfoReferences.add(refIdOfficeObject);
        List<XMLObject> signatureObjects = new Vector<XMLObject>();
        XMLObject idPackageObject;
        try {
            idPackageObject = CreateIdPackageObject(fac, PackageDigitalSignature.DefaultSignatureId, doc, CalculateIdPackageObjectReferences(pSigningPrivateKey, pSigningCertificate));
        } catch (Exception e) {
            throw new OpenXML4JException("Error constructing idPackageObject", e);
        }
        XMLObject idOfficeObject = CreateIdOfficeObject(fac, PackageDigitalSignature.DefaultSignatureId, doc);
        signatureObjects.add(idPackageObject);
        signatureObjects.add(idOfficeObject);
        SignedInfo si;
        try {
            si = fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null), fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null), signedInfoReferences);
        } catch (NoSuchAlgorithmException e) {
            throw new OpenXML4JException("XML signing algorithm error", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new OpenXML4JException("XML signing algorithm parameters error", e);
        }
        KeyInfo ki = null;
        KeyInfoFactory kif = fac.getKeyInfoFactory();
        KeyValue kv;
        try {
            kv = kif.newKeyValue(pSigningCertificate.getPublicKey());
        } catch (KeyException e) {
            throw new OpenXML4JException("Problem obtaining public key from certificate", e);
        }
        X509Data x509d = kif.newX509Data(Collections.singletonList(pSigningCertificate));
        List<XMLStructure> keyInfoContents = new Vector<XMLStructure>();
        keyInfoContents.add(kv);
        keyInfoContents.add(x509d);
        ki = kif.newKeyInfo(keyInfoContents);
        XMLSignature signature = fac.newXMLSignature(si, ki, signatureObjects, PackageDigitalSignature.DefaultSignatureId, null);
        DOMSignContext dsc = new DOMSignContext(pSigningPrivateKey, doc);
        dsc.setURIDereferencer(new OPCURIDereferencer(_container, fac.getURIDereferencer()));
        try {
            signature.sign(dsc);
        } catch (MarshalException e) {
            throw new OpenXML4JException("Error signing XML", e);
        } catch (XMLSignatureException e) {
            throw new OpenXML4JException("XMLSignature Exception when signing", e);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans;
        try {
            trans = tf.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new OpenXML4JException("Problem creating Transformer for output", e);
        }
        trans.setOutputProperty(OutputKeys.INDENT, "no");
        trans.setOutputProperty(OutputKeys.STANDALONE, "yes");
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            trans.transform(new DOMSource(doc), new StreamResult(bout));
        } catch (TransformerException e) {
            throw new OpenXML4JException("Problem transforming output to output stream", e);
        }
        CreateDigitalSignatureSignaturePartFromContent(bout);
    }
