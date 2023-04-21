    public static String digest(final String all) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(all.getBytes());
            return new String(hexCodes(bytes));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MD5String.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
