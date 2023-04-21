    private static byte[] getBytesFromStream(InputStream is) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 512];
        while (true) {
            int read = is.read(buffer);
            if (read == -1) break;
            stream.write(buffer, 0, read);
        }
        is.close();
        return stream.toByteArray();
    }
