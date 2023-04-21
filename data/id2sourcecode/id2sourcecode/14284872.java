    public CertId(X509Certificate issuerCert, SerialNumber serialNumber) throws IOException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException nsae) {
            throw new IOException("Unable to create CertId", nsae);
        }
        hashAlgId = SHA1_ALGID;
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
            System.out.println("SerialNumber is " + serialNumber.getNumber());
        }
    }
