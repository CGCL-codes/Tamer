    public static String getHash(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = str.getBytes("utf8");
            md.update(array);
            byte[] temp;
            temp = md.digest();
            String result = "";
            for (int i = 0; i < temp.length; i++) {
                result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
            }
            return result;
        } catch (Exception e) {
        }
        return null;
    }
