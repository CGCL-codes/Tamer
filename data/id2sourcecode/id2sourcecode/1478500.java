    public void parse(final String dbtype, final int readlines, final int writelines) {
        this.getSpreparser().parse(readlines);
        this.getSparser().parse(writelines);
        System.gc();
        this.getCparser().parse(writelines);
    }
