    static byte[] passwordHashStage1(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        StringBuffer cleansedPassword = new StringBuffer();
        int passwordLength = password.length();
        for (int i = 0; i < passwordLength; i++) {
            char c = password.charAt(i);
            if ((c == ' ') || (c == '\t')) {
                continue;
            }
            cleansedPassword.append(c);
        }
        return md.digest(cleansedPassword.toString().getBytes());
    }
