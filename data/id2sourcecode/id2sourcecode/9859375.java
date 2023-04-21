    public KBLocation(String url, String password, boolean isPasswordStored) {
        this(url, password, Engine.digest(password, url), true, isPasswordStored);
    }
