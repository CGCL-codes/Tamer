    public static String getMd5Hash(String text) throws NoSuchAlgorithmException {
        MessageDigest messageDigest;
        messageDigest = MessageDigest.getInstance("MD5");
        byte[] md5 = messageDigest.digest(text.getBytes());
        String md5Text = "";
        for (byte element : md5) {
            String temp = Integer.toHexString(0xFF & element);
            if (temp.length() < 2) {
                temp = "0" + temp;
            }
            md5Text += temp;
        }
        text = md5Text;
        return text;
    }
