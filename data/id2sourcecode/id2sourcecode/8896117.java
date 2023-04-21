    private String getPwdMD5() {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            char[] passwd = pwdPassword.getPassword();
            String aString = new String(passwd);
            byte[] bytes = aString.getBytes();
            md.update(bytes);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Unknown Algorithm");
            System.exit(0);
        }
        byte[] md5byte = md.digest();
        String md5 = byteArrayToHexString(md5byte);
        return md5;
    }
