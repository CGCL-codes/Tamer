    public static ByteArrayOutputStream getContents(final InputStream inputStream, final long maxLength) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (inputStream == null) {
            return byteArrayOutputStream;
        }
        try {
            byte[] bytes = new byte[1024];
            int read = inputStream.read(bytes);
            while (read > -1 && byteArrayOutputStream.size() < maxLength) {
                byteArrayOutputStream.write(bytes, 0, read);
                read = inputStream.read(bytes);
            }
        } catch (Exception e) {
            LOGGER.error("Exception accessing the file contents : " + inputStream, e);
        } finally {
            close(inputStream);
        }
        return byteArrayOutputStream;
    }
