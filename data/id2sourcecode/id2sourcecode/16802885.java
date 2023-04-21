    protected void openFile() throws IOException {
        String filename = getFilename();
        FileOutputStream fos = new FileOutputStream(filename, !isOverwrite());
        fileChannel = fos.getChannel();
        String header = getFileHeader();
        if (!header.isEmpty()) {
            fileChannel.write(ByteBuffer.wrap(header.getBytes()));
        }
    }
