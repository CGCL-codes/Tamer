    public static ByteArrayOutputStream getContentsFromEnd(final File file, final long bytesToRead) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FileInputStream fileInputStream = null;
        try {
            long length = file.length();
            fileInputStream = new FileInputStream(file);
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) bytesToRead);
            long position = length - bytesToRead;
            if (position < 0) {
                position = 0;
            }
            fileInputStream.getChannel().read(byteBuffer, position);
            byteArrayOutputStream.write(byteBuffer.array());
        } catch (Exception e) {
            LOGGER.error("Exception reading from the end of the file : ", e);
        } finally {
            close(fileInputStream);
        }
        return byteArrayOutputStream;
    }
