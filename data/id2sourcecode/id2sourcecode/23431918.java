    protected byte[] computeSessionKey(byte[] sharedSecret, byte[] clientChallenge, byte[] serverChallenge) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] zeroes = { 0, 0, 0, 0 };
            messageDigest.update(zeroes, 0, 4);
            messageDigest.update(clientChallenge, 0, 8);
            messageDigest.update(serverChallenge, 0, 8);
            HMACT64 hmact64 = new HMACT64(sharedSecret);
            hmact64.update(messageDigest.digest());
            return hmact64.digest();
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }
        return null;
    }
