    public static String encrypt(String str) throws EntityException {
        try {
            byte[] strTemp = str.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] chars = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                chars[k++] = DIGITS[byte0 >>> 4 & 0xf];
                chars[k++] = DIGITS[byte0 & 0xf];
            }
            return new String(chars);
        } catch (NoSuchAlgorithmException e) {
            throw new EntityException(e);
        }
    }
