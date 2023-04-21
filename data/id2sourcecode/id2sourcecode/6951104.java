    public ValidEPoint validate(EPoint e) throws MalformedURLException, IOException, NoSuchAlgorithmException, InvalidEPointCertificateException, InvalidKeyException, SignatureException {
        InputStream is = new URL(url, "info?ID=" + Base16.encode(e.getMD())).openStream();
        ValidEPoint v = new ValidEPoint(this, e, is);
        is.close();
        return v;
    }
