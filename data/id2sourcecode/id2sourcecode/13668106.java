    private static String sha1(String password) {
        String res = "";
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            byte[] digest = sha1.digest((password).getBytes());
            res = bytes2String(digest);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return res;
    }
