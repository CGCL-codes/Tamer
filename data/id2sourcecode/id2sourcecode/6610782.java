    public BigFileGenerator(String filename, int bufferSize, long crapSize) throws IOException {
        this.crapSize = crapSize;
        final File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        fileOutputStream = new FileOutputStream(file);
        fileChannel = fileOutputStream.getChannel();
        byteBuffer = ByteBuffer.allocateDirect(bufferSize);
        for (int i = 0; i < byteBuffer.capacity(); i++) {
            byteBuffer.put(CRAP);
        }
        byteBuffer.rewind();
    }
