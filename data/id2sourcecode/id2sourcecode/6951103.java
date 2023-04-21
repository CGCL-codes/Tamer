    public IssuerStats getStats() throws MalformedURLException, IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        InputStream is = new URL(url, "info").openStream();
        IssuerStats s = new IssuerStats(is, this.getPublicKey());
        is.close();
        return s;
    }
