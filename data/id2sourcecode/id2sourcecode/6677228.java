    private String[] getResponses(byte[] magicKey, String password, boolean doModify) throws NoSuchAlgorithmException, NoSuchProviderException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] result = md5.digest(password.getBytes());
        byte[] hash = Base64.toYmsgBase64(result);
        byte[] digest2 = getDigest(magicKey, hash, doModify);
        String response6 = getResponse(digest2);
        md5 = MessageDigest.getInstance("MD5");
        result = Crypt.crypt(password, "$1$_2S43d5f$");
        result = md5.digest(result);
        hash = Base64.toYmsgBase64(result);
        digest2 = getDigest(magicKey, hash, doModify);
        String response96 = getResponse(digest2);
        return new String[] { response6, response96 };
    }
