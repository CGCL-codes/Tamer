    public byte[] evaluateEvidence(byte[] peerEvidence) throws SaslException {
        byte[] key = K.getEncoded();
        int t = key[0];
        for (int i = 0; i < key.length - 1; i++) key[i] = key[i + 1];
        key[key.length - 1] = (byte) t;
        K = new SRPSecretKey(key);
        return ((SRPContext) getSaslSecurityContext()).getSignature();
    }
