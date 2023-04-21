    public KBLocation(String url, String password) {
        this(url, password, Engine.digest(password, url), true, false);
    }
