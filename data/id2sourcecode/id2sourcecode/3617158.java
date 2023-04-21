    public byte[] computeProxy() throws CertificateEncodingException, ContentEncodingException {
        if (null == content()) return null;
        if ((null == signature()) || (null == signature().witness())) {
            return null;
        }
        byte[] blockDigest = CCNDigestHelper.digest(prepareContent(name(), signedInfo(), content()));
        return signature().computeProxy(blockDigest, true);
    }
