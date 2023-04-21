    public byte[] generateSignature(String PIN, String pinID, String bsoId, String seID, byte[] contentbytes, Session session) throws CardException, IOException, NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());
        MessageDigest hash = MessageDigest.getInstance("SHA1");
        hash.update(contentbytes);
        byte[] digest = hash.digest();
        return sign(PIN, pinID, seID, bsoId, digest, session);
    }
