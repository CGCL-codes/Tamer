    public void setPassword(String password) {
        this.password = password;
        pwdChecksum = CryptoHelper.digest(password);
    }
