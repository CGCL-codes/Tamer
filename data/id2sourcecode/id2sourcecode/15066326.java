    public void configure(String spec) throws IOException {
        resource = spec;
        imageOffset = 0;
        InputStream in = ArrayBackedSeekableIODevice.class.getResourceAsStream(resource);
        if (in == null) {
            LOGGING.log(Level.SEVERE, "resource not found: {0}", resource);
            throw new IOException("resource not found: " + resource);
        }
        try {
            byte[] buffer = new byte[1024];
            ExposedByteArrayOutputStream bout = new ExposedByteArrayOutputStream(32 * 1024);
            while (true) {
                int read = in.read(buffer);
                if (read < 0) break;
                bout.write(buffer, 0, read);
            }
            imageData = bout.getBuffer();
            length = bout.getPosition();
        } catch (IOException e) {
            LOGGING.log(Level.SEVERE, "could not load file", e);
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }
