    public void verify(byte[] signature, byte[] message) throws AuthException, InvalidKeyFormatException {
        if (signature.length > MAX_SIGNATURE_LEN) throw new AuthException("Signature expected to be at most " + MAX_SIGNATURE_LEN + " long");
        Debug.println("gale.verify", "PublicKey.verify() message");
        Debug.println("gale.verify", message);
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Debug.assrt(false);
        }
        byte[] digest = messageDigest.digest(message);
        byte[] digestInfo = encodeDigestInfo(digest, DA_MD5);
        Debug.println("gale.verify", "PublicKey.verify() calculated digest");
        Debug.println("gale.verify", digestInfo);
        try {
            byte[] originalDigestInfo = publicDecrypt(signature).toByteArray();
            Debug.println("gale.verify", "PublicKey.verify() attached digest");
            Debug.println("gale.verify", originalDigestInfo);
            if (digestInfo.length != originalDigestInfo.length) throw new AuthException("Original Digest not the same size as " + " calculated digest"); else {
                for (int i = 0; i < digestInfo.length; ++i) {
                    if (digestInfo[i] != originalDigestInfo[i]) throw new AuthException("Mismatch in byte " + i);
                }
            }
        } catch (InvalidKeyFormatException e) {
            throw new AuthException(e.getMessage());
        }
    }
