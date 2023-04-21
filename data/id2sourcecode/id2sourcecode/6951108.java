    public EPointCertificate getCertificate(long sn) throws MalformedURLException, IOException, NoSuchAlgorithmException, InvalidEPointCertificateException, InvalidKeyException, SignatureException {
        InputStream is = new URL(url, EPointCertificate.serial(sn)).openStream();
        EPointCertificate v = new EPointCertificate(is, this.getPublicKey());
        is.close();
        if (v.getSN() != sn) throw new InvalidEPointCertificateException("SN mismatch: " + sn + "!=" + v.getSN());
        return v;
    }
