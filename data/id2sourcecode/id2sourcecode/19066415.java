    public MD5(String string) {
        this.key = this.digest(string).toString();
    }
