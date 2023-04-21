    public static byte[] sign(String digestAlgorithm, byte[][] toBeSigneds, PrivateKey signingKey) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        if (null == toBeSigneds) {
            Log.info("sign: null content to be signed!");
            throw new SignatureException("Cannot sign null content!");
        }
        if (null == signingKey) {
            Log.info("sign: Signing key cannot be null.");
            Log.info("Temporarily generating fake signature.");
            return DigestHelper.digest(digestAlgorithm, toBeSigneds);
        }
        String sigAlgName = getSignatureAlgorithmName(((null == digestAlgorithm) || (digestAlgorithm.length() == 0)) ? DigestHelper.DEFAULT_DIGEST_ALGORITHM : digestAlgorithm, signingKey);
        Signature sig = Signature.getInstance(sigAlgName);
        SignatureLocks.signingLock();
        try {
            sig.initSign(signingKey);
            for (int i = 0; i < toBeSigneds.length; ++i) {
                sig.update(toBeSigneds[i]);
            }
            return sig.sign();
        } finally {
            SignatureLocks.signingUnock();
        }
    }
