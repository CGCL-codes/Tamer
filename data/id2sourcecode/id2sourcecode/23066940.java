    public static final Access getAccess(final File file, final boolean read, final boolean write) throws FileNotFoundException {
        return new AccessImpl(file, read, write);
    }
