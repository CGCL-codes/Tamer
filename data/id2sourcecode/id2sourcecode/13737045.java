    public CertId(X509CertImpl issuerCert, SerialNumber serialNumber) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        hashAlgId = AlgorithmId.get("SHA1");
        md.update(issuerCert.getSubjectX500Principal().getEncoded());
        issuerNameHash = md.digest();
        byte[] pubKey = issuerCert.getPublicKey().getEncoded();
        DerValue val = new DerValue(pubKey);
        DerValue[] seq = new DerValue[2];
        seq[0] = val.data.getDerValue();
        seq[1] = val.data.getDerValue();
        byte[] keyBytes = seq[1].getBitString();
        md.update(keyBytes);
        issuerKeyHash = md.digest();
        certSerialNumber = serialNumber;
        if (debug) {
            HexDumpEncoder encoder = new HexDumpEncoder();
            System.out.println("Issuer Certificate is " + issuerCert);
            System.out.println("issuerNameHash is " + encoder.encode(issuerNameHash));
            System.out.println("issuerKeyHash is " + encoder.encode(issuerKeyHash));
        }
    }
