    private byte[] unzipFromStream(final InputStream sourceStream) throws IOException {
        ZipInputStream zipStream = null;
        byte[] result = new byte[] {};
        try {
            zipStream = new ZipInputStream(sourceStream);
            zipStream.getNextEntry();
            final int bufferSize = 10240;
            byte[] buffer = new byte[bufferSize];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            while (zipStream.available() > 0) {
                int readBytes = zipStream.read(buffer, 0, bufferSize);
                if (readBytes > 0) {
                    outputStream.write(buffer, 0, readBytes);
                }
            }
            outputStream.flush();
            result = outputStream.toByteArray();
            outputStream.close();
        } catch (IOException e) {
            LOG.error("Error while uncompressing data", e);
        } finally {
            if (zipStream != null) zipStream.close();
        }
        return result;
    }
