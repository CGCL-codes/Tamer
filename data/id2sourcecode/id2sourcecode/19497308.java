    public HttpPrincipal(String url, String userId, String password, boolean digested) {
        _url = url;
        _userId = userId;
        if (digested) {
            _password = password;
        } else {
            _password = Encryptor.digest(password);
        }
    }
