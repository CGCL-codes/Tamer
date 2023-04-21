    private EPointCertificate getCertificate(byte[] id, boolean obligation) throws MalformedURLException, IOException, NoSuchAlgorithmException, InvalidEPointCertificateException, InvalidKeyException, SignatureException {
        InputStream is = new URL(url, (obligation ? "info?KEY=" : "info?ID=") + Base16.encode(id)).openStream();
        EPointCertificate v = new EPointCertificate(is, this.getPublicKey());
        is.close();
        if (Base16.encode(id).equals(Base16.encode(v.getID()))) return v;
        throw new InvalidEPointCertificateException("ID mismatch: " + Base16.encode(id) + "!=" + Base16.encode(v.getID()));
    }
