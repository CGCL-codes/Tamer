    public LeedorFileChannel(File file) throws FileNotFoundException {
        FileInputStream fi = new FileInputStream(file);
        reader = fi.getChannel();
    }
