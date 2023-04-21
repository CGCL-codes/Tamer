    public byte[] signAuthn(byte[] toBeSigned, boolean requireSecureReader) throws NoSuchAlgorithmException, CardException, IOException, InterruptedException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        byte[] digest = messageDigest.digest(toBeSigned);
        byte keyId = AUTHN_KEY_ID;
        byte[] signatureValue = sign(digest, "SHA-1", keyId, requireSecureReader);
        return signatureValue;
    }
