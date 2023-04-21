    private static String crypt(String pwd, String algorithm) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        StringBuffer password = new StringBuffer();
        password.append("{").append(algorithm).append("}");
        password.append(Text.digest(algorithm, pwd.getBytes("UTF-8")));
        return password.toString();
    }
