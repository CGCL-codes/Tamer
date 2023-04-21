    public ArrayBackedSeekableIODevice(String name, InputStream data) throws IOException {
        imageOffset = 0;
        resource = name;
        byte[] buffer = new byte[8 * 1024];
        ExposedByteArrayOutputStream bout = new ExposedByteArrayOutputStream(32 * 1024);
        while (true) {
            int read = data.read(buffer);
            if (read < 0) break;
            bout.write(buffer, 0, read);
        }
        data.close();
        imageData = bout.getBuffer();
        length = bout.getPosition();
    }
