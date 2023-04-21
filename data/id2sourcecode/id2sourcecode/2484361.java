    public Hash finish() {
        bytes = md.digest();
        return this;
    }
