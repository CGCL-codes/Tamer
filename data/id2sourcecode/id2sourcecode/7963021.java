    public static String makeHash(byte[] challenge, byte[] password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(challenge);
            md.update(password);
            return byteArrayToHexString(md.digest());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("" + ex);
        }
    }
