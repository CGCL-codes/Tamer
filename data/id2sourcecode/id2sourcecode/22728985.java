    @Override
    protected void engineUpdate(byte[] b, int off, int len) throws SignatureException {
        LOG.debug("engineUpdate(b,off,len): off=" + off + "; len=" + len);
        this.messageDigest.update(b, off, len);
        byte[] digestValue = this.messageDigest.digest();
        byte[] expectedDigestValue = SHA1WithRSAProxySignature.digestValues.get();
        if (null == expectedDigestValue) {
            SHA1WithRSAProxySignature.digestValues.set(digestValue);
        } else {
            if (false == Arrays.areEqual(expectedDigestValue, digestValue)) {
                throw new IllegalStateException("digest value has changed");
            }
        }
        LOG.debug("digest value: " + Hex.encodeHexString(digestValue));
    }
