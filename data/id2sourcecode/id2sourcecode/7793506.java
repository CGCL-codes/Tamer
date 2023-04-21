    @Override
    public String passwordEncode(String password, String encoding) {
        return messageDigest.digest(password, encoding);
    }
