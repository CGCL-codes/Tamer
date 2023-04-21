    public String md5(String str) {
        String result = null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(md5);
            result = byteToString(md.digest(str.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            JangodLogger.severe(ex.getMessage());
        }
        return result;
    }
