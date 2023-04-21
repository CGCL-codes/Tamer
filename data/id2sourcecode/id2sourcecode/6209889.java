    public byte[] toByteArray() {
        InputStream dataStream = stream.getInputStream();
        ByteArrayOutputStream bis = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int read;
        try {
            read = dataStream.read(buffer);
            while (read > 0) {
                bis.write(buffer, 0, read);
                read = dataStream.read(buffer);
            }
            return bis.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }
