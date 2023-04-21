    public ReadableByteChannel openChannel(Object file) throws IOException {
        return new FileInputStream((File) file).getChannel();
    }
