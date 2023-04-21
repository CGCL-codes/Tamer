    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        if (outstream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        FileChannel fileChannel = new FileInputStream(this.file).getChannel();
        WritableByteChannel outputChannel = Channels.newChannel(outstream);
        long fileSize = file.length(), position = 0;
        try {
            while (position < fileSize) {
                position += fileChannel.transferTo(position, fileSize, outputChannel);
            }
        } finally {
            fileChannel.close();
        }
    }
