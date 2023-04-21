    public User(String pDescription, String pPassword, String pFullName) {
        this.description = pDescription;
        Digest di = new Digest();
        this.password = di.digest(pPassword);
        this.fullname = pFullName;
    }
