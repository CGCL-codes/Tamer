    static String encryptPassword(String password) {
        MessageDigest md5 = null;
        StringBuffer cacheChar = new StringBuffer();
        try {
            byte defaultByte[] = password.getBytes();
            md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(defaultByte);
            byte resultByte[] = md5.digest();
            for (int i = 0; i < resultByte.length; i++) {
                String hex = Integer.toHexString(ENCRYPTKEY & resultByte[i]);
                if (hex.length() == 1) {
                    cacheChar.append("c");
                }
                cacheChar.append(hex);
            }
            return cacheChar.toString();
        } catch (Exception e) {
            System.out.println("Encrypt Passwords Failed");
            e.printStackTrace();
            return null;
        }
    }
