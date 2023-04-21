    private byte[] encryptPassword(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
        }
        byte[] pw = password.getBytes();
        byte[] saltAndPw = new byte[salt.length + pw.length];
        System.arraycopy(salt, 0, saltAndPw, 0, salt.length);
        System.arraycopy(pw, 0, saltAndPw, salt.length, pw.length);
        if (md != null) for (int i = 0; i < 5; i++) saltAndPw = md.digest(saltAndPw);
        return saltAndPw;
    }
