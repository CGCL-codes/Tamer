    private String encrypt(String password) {
        if (password == null) password = "";
        if (_cryptMethod == MD5) return asHexString(_digest.digest(password.getBytes())); else return password;
    }
