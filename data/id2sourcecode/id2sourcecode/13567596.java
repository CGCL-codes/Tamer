    public boolean verify(URL url, Signature signatureData) throws SignatureProviderException, IOException, SignatureVerifyException {
        return verify(url.openStream(), signatureData);
    }
