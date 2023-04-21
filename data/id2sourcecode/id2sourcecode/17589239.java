    public BlowFishKey(byte[] blowfishKey, RSAPublicKey publicKey) {
        writeC(0x00);
        byte[] encrypted = null;
        try {
            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encrypted = rsaCipher.doFinal(blowfishKey);
        } catch (GeneralSecurityException e) {
            _log.severe("Error While encrypting blowfish key for transmision (Crypt error)");
            e.printStackTrace();
        }
        writeD(encrypted.length);
        writeB(encrypted);
    }
