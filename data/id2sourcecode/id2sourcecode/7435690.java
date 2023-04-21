    public void appendClassProfile(final Class c, final long read, final long write) {
        profiles = profiles.addClassProfile(c, read, write);
    }
