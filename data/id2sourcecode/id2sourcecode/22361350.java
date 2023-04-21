    private File createLargeFile() throws IOException {
        File largeFile = File.createTempFile("mina-test", "largefile");
        largeFile.deleteOnExit();
        FileChannel channel = new FileOutputStream(largeFile).getChannel();
        ByteBuffer buffer = createBuffer();
        channel.write(buffer);
        channel.close();
        return largeFile;
    }
