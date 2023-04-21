    private static byte[] getHashedIntegrityCode(byte[] iv, byte[] integrityKey, String clearText) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] clearBytes = new byte[0];
        clearBytes = clearText.getBytes("UTF8");
        byte[] lastBlock = new byte[16];
        System.arraycopy(clearBytes, clearBytes.length - 16, lastBlock, 0, 16);
        MessageDigest digest = null;
        digest = MessageDigest.getInstance("SHA-256");
        byte[] integrityCheck = new byte[64];
        System.arraycopy(iv, 0, integrityCheck, 0, 16);
        System.arraycopy(integrityKey, 0, integrityCheck, 16, 32);
        System.arraycopy(lastBlock, 0, integrityCheck, 48, 16);
        digest.update(integrityCheck);
        return digest.digest();
    }
