    public SecurityId(String username, String password, boolean bEncode) {
        Util.argCheckEmpty(username, USERNAME.getText());
        Util.argCheckNull(password, PASSWORD.getText());
        _username = username;
        if (bEncode) try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte digestBytes[] = md.digest(password.getBytes());
            _password = Util.bytesToHex(digestBytes);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.toString());
        } else _password = password;
    }
