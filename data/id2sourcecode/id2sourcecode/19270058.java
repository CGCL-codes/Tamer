    public void addUserCredential(String login, String password) throws UnsupportedEncodingException {
        byte[] credential = new byte[64];
        System.arraycopy(sha256Digester.digest(login.getBytes("UTF-8")), 0, credential, 0, 32);
        System.arraycopy(sha256Digester.digest(password.getBytes("UTF-8")), 0, credential, 32, 32);
        userCredentials.put(credential, login);
    }
