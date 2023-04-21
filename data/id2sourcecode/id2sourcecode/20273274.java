    public void setPassword(String pPassword) throws NoSuchAlgorithmException {
        Digest di = new Digest();
        this.password = di.digest(pPassword);
    }
