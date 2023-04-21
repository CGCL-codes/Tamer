    private static void copyStream(InputStream inputStream, FileOutputStream outputStream) throws IOException, StorageException {
        byte[] buffer = new byte[4096];
        int read = 0;
        while ((read = inputStream.read(buffer, 0, buffer.length)) > 0) {
            try {
                outputStream.write(buffer, 0, read);
            } catch (Exception e) {
                throw new StorageException(e.getMessage(), e);
            }
        }
    }
