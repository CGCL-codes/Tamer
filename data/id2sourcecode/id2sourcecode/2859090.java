    public static String encryptPassword(String data) throws Exception {
        if (data == null) {
            return "";
        }
        byte[] plainText = null;
        byte[] hashValue = null;
        plainText = data.getBytes();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        hashValue = md.digest(plainText);
        return new String(Base64.encodeBase64(hashValue));
    }
