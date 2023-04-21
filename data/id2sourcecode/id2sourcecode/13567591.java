    public Signature sign(URL url, String certificateName, String password, String contentType) throws IOException, SignatureException {
        return sign(url.openStream(), certificateName, password, contentType);
    }
