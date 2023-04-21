    public boolean verifyAPosterioriTimeStamp(URL url, Signature signatureData) throws SignatureProviderException, IOException, SignatureVerifyException {
        return verifyAPosterioriTimeStamp(url.openStream(), signatureData);
    }
