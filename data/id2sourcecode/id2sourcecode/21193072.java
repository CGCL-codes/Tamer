    public LazyRenameRAF(RAF raf) throws IOException {
        super(raf);
        if (getMode().equals("r")) {
            throw new IllegalStateException("LazyRenameRAFs are only useful " + "in read/write mode.");
        }
    }
