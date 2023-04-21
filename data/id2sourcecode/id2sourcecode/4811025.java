    public static void main(String[] args) {
        MessageDigest sha1 = null;
        try {
            sha1 = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
        }
        String hash = bytes2String(sha1.digest(("password-salt").getBytes()));
        System.out.println(hash);
    }
