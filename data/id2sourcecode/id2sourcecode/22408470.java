    protected byte[] getDigestValue() throws SignatureException {
        needsReset = false;
        return messageDigest.digest();
    }
