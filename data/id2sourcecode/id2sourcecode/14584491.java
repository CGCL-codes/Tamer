    @Override
    public FileChannel getChannel() throws IOException {
        try {
            return new FileInputStream(file).getChannel();
        } catch (FileNotFoundException fnfe) {
            throw new IOException("Couldn't get the channel. File not found");
        }
    }
