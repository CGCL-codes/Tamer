    public void setContent(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new NullPointerException("inputStream");
        }
        if (this.file != null) {
            delete();
        }
        file = tempFile();
        FileOutputStream outputStream = new FileOutputStream(file);
        FileChannel localfileChannel = outputStream.getChannel();
        byte[] bytes = new byte[4096 * 4];
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        int read = inputStream.read(bytes);
        int written = 0;
        while (read > 0) {
            byteBuffer.position(read).flip();
            written += localfileChannel.write(byteBuffer);
            read = inputStream.read(bytes);
        }
        localfileChannel.force(false);
        localfileChannel.close();
        size = written;
        if (definedSize > 0 && definedSize < size) {
            file.delete();
            file = null;
            throw new IOException("Out of size: " + size + " > " + definedSize);
        }
        isRenamed = true;
        completed = true;
    }
