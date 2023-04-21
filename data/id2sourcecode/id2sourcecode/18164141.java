    public String encode(String password) throws UnsupportedEncodingException {
        String encodedString = "";
        if (password != null) {
            byte[] hash = md.digest(password.getBytes("ASCII"));
            encodedString = encoder.encode(hash);
        }
        return encodedString;
    }
