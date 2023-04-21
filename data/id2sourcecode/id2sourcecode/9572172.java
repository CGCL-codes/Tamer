    private InputStream getStreamWithMarkReset(InputStream stream) throws IOException {
        if (stream.markSupported()) {
            return stream;
        }
        ByteArrayOutputStream output = null;
        try {
            output = new ByteArrayOutputStream(stream.available());
            byte[] buf = new byte[2048];
            int read;
            while ((read = stream.read(buf)) > 0) {
                output.write(buf, 0, read);
            }
            return new ByteArrayInputStream(output.toByteArray());
        } finally {
            try {
                stream.close();
            } catch (IOException ignored) {
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
