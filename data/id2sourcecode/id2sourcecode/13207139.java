    public FileChannel getInputChannel() throws FileNotFoundException {
        FileInputStream is = new FileInputStream(this);
        return is.getChannel();
    }
