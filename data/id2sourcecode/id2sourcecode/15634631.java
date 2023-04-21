    public static String hashSHA(String str) {
        byte[] b = str.getBytes();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return (base64Encode(md.digest()));
    }
