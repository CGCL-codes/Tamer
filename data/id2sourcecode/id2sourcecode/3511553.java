    public static byte[] sign(String digestAlgorithm, byte[] toBeSigned, PrivateKey signingKey) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        if (null == toBeSigned) {
            Log.info("sign: null content to be signed!");
            throw new SignatureException("Cannot sign null content!");
        }
        if (null == signingKey) {
            Log.info("sign: Signing key cannot be null.");
            Log.info("Temporarily generating fake signature.");
            return DigestHelper.digest(digestAlgorithm, toBeSigned);
        }
        String sigAlgName = getSignatureAlgorithmName(((null == digestAlgorithm) || (digestAlgorithm.length() == 0)) ? DigestHelper.DEFAULT_DIGEST_ALGORITHM : digestAlgorithm, signingKey);
        Signature sig = Signature.getInstance(sigAlgName);
        SignatureLocks.signingLock();
        try {
            sig.initSign(signingKey);
            sig.update(toBeSigned);
            return sig.sign();
        } finally {
            SignatureLocks.signingUnock();
        }
    }
