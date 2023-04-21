    public DirProperties(String location, boolean write, boolean read) {
        if (location.equals(USER)) {
        } else if (location.equals(DEV)) {
        } else if (location.equals(ADMIN)) {
        } else throw new UnsupportedOperationException("location should be one of the variables: DEV, ADMIN or USER");
        this.write = write;
        this.read = read;
    }
