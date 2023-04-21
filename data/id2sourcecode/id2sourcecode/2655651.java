    private String buildMD5Hash(String s) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            return asHex(md.digest(s.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace(System.err);
        }
        return "";
    }
