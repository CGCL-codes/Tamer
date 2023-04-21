    public static byte[] getHash(byte[] thisBinaryData) {
        try {
            if (md == null) md = MessageDigest.getInstance("SHA");
            md.update(thisBinaryData);
            return md.digest();
        } catch (NoSuchAlgorithmException ex) {
            GlobalLog.logError(ex);
        }
        return null;
    }
