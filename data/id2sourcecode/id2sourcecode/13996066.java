    public byte[] getSigndata() throws NoSuchAlgorithmException, NoSuchProviderException, IOException, CMSException, CertificateEncodingException {
        String digestAlgOID = CMSSignedGenerator.DIGEST_SHA1;
        String encAlgOID = CMSSignedGenerator.ENCRYPTION_RSA;
        MessageDigest dig = MessageDigest.getInstance(digestAlgOID, "BC");
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        content.write(bo);
        dig.update(bo.toByteArray());
        byte[] contentDigest = dig.digest();
        DEREncodableVector v = new ASN1EncodableVector();
        v.add(new Attribute(PKCSObjectIdentifiers.pkcs_9_at_contentType, new DERSet(PKCSObjectIdentifiers.data)));
        v.add(new Attribute(PKCSObjectIdentifiers.pkcs_9_at_signingTime, new DERSet(new DERUTCTime(signDate))));
        v.add(new Attribute(PKCSObjectIdentifiers.pkcs_9_at_messageDigest, new DERSet(new DEROctetString(contentDigest))));
        if (certificate != null) {
            dig.reset();
            ESSCertID essCertid = new ESSCertID(dig.digest(certificate.getEncoded()));
            v.add(new Attribute(PKCSObjectIdentifiers.id_aa_signingCertificate, new DERSet(new SigningCertificate(essCertid))));
        }
        signedAttr = new DERSet(v);
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        DEROutputStream dOut = new DEROutputStream(bOut);
        dOut.writeObject(signedAttr);
        byte[] dataToBeSigned = bOut.toByteArray();
        dig.reset();
        dig.update(dataToBeSigned);
        dataToBeSignedDigest = dig.digest();
        AlgorithmIdentifier algId = new AlgorithmIdentifier(new DERObjectIdentifier(digestAlgOID), new DERNull());
        contentDigestInfo = new DigestInfo(algId, dataToBeSignedDigest);
        return dataToBeSigned;
    }
