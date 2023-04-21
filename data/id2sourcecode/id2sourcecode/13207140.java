    public FileChannel getOutputChannel() throws FileNotFoundException {
        FileOutputStream os = new FileOutputStream(this);
        return os.getChannel();
    }
