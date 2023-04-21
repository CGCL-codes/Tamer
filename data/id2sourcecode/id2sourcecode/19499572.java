    public static String encodeToMD5(String str) {
        if (str == null) {
            return null;
        }
        String digstr = "";
        MessageDigest MD = null;
        try {
            MD = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] oldbyte = new byte[str.length()];
        for (int i = 0; i < str.length(); i++) {
            oldbyte[i] = (byte) str.charAt(i);
        }
        MD.update(oldbyte);
        byte[] newbyte = null;
        newbyte = MD.digest(oldbyte);
        for (int i = 0; i < newbyte.length; i++) {
            digstr = digstr + newbyte[i];
        }
        return digstr;
    }
