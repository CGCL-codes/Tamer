    public Authenticate(String username, String password) throws IOException {
        MessageDigest md;
        this.username = username;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException ex) {
            throw new IOException("Your system does not support the SHA algorithm.");
        }
        try {
            passwordHash = md.digest(password.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new IOException("Failed to encode the password into bytes.");
        }
    }
