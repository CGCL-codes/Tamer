    private Attribute buildSigningCertificateV2Attribute(String sigProvider) throws NoSuchAlgorithmException, NoSuchProviderException, CertificateEncodingException, IOException {
        X509Certificate cert = this.getCertificate();
        MessageDigest dig = MessageDigest.getInstance(this.getDigestAlgOID(), sigProvider);
        byte[] certHash = dig.digest(cert.getEncoded());
        JcaX509CertificateHolder holder = new JcaX509CertificateHolder(cert);
        X500Name x500name = holder.getIssuer();
        GeneralName generalName = new GeneralName(x500name);
        GeneralNames generalNames = new GeneralNames(generalName);
        DERInteger serialNum = new DERInteger(holder.getSerialNumber());
        IssuerSerial issuerserial = new IssuerSerial(generalNames, serialNum);
        ESSCertIDv2 essCert = new ESSCertIDv2(new AlgorithmIdentifier(getDigestAlgOID()), certHash, issuerserial);
        SigningCertificateV2 scv2 = new SigningCertificateV2(new ESSCertIDv2[] { essCert });
        return new Attribute(PKCSObjectIdentifiers.id_aa_signingCertificateV2, new DERSet(scv2));
    }
