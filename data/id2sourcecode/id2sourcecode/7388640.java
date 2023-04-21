    public String hashPassword(String password) {
        messageDigest.reset();
        byte[] bytes = messageDigest.digest(password.getBytes());
        String md5 = "";
        for (byte b : bytes) {
            String str = Integer.toHexString(b);
            if (str.length() == 1) {
                str = "0" + str;
            }
            md5 += str.substring(str.length() - 2);
        }
        return md5;
    }
