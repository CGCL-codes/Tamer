    private static ByteArray inputStreamToByteArray(InputStream istream) throws IOException {
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        byte buffer[] = new byte[BUFFER_SIZE];
        int read;
        while ((read = istream.read(buffer)) >= 0) {
            ostream.write(buffer, 0, read);
        }
        return new ByteArray(ostream.toByteArray());
    }
