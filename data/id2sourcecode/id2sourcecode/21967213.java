    public static byte[] getBytesFromStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        boolean reading = true;
        while (reading) {
            int read = is.read(buffer);
            if (read == -1) {
                reading = false;
            } else {
                baos.write(buffer, 0, read);
            }
        }
        return baos.toByteArray();
    }
