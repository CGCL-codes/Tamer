    private static String md5Password(String login, String password) {
        String hash = "";
        String md5Password = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            hash = login + ":" + password;
            byte[] rawPass = hash.getBytes();
            try {
                md.update(rawPass);
            } catch (Exception e) {
                CofaxToolsUtil.log("CofaxToolsLogin login : " + e);
            }
            md5Password = toHex(md.digest());
        } catch (NoSuchAlgorithmException nsae) {
            CofaxToolsUtil.log("CofaxToolsLogin login : " + nsae);
        }
        return md5Password;
    }
