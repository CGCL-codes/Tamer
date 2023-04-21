    public static String getEncryptedPassword(String password) throws InfoException {
        StringBuffer buffer = new StringBuffer();
        try {
            byte[] encrypt = password.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(encrypt);
            byte[] hashedPasswd = md.digest();
            for (int i = 0; i < hashedPasswd.length; i++) {
                buffer.append(Byte.toString(hashedPasswd[i]));
            }
        } catch (Exception e) {
            throw new InfoException(LanguageTraslator.traslate("474"), e);
        }
        return buffer.toString();
    }
