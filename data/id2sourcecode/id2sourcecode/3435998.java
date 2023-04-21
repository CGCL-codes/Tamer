    public static void getContents(final InputStream inputStream, final OutputStream outputStream, final long maxLength) {
        if (inputStream == null) {
            return;
        }
        try {
            int total = 0;
            byte[] bytes = new byte[1024];
            int read = inputStream.read(bytes);
            while (read > -1 && total < maxLength) {
                total += read;
                outputStream.write(bytes, 0, read);
                read = inputStream.read(bytes);
            }
        } catch (Exception e) {
            LOGGER.error("Exception accessing the stream contents.", e);
        } finally {
            close(inputStream);
        }
    }
