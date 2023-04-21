    @Override
    public FileChannel getChannel() throws IOException {
        try {
            return new FileInputStream(this.file).getChannel();
        } catch (FileNotFoundException fnfe) {
            throw new IOException("Couldn't get the channel. File not found");
        }
    }
