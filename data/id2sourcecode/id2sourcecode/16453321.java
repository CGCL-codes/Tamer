    public static byte[] readFully(InputStream in, int bufferLength) throws IOException {
        byte[] buffer = new byte[bufferLength];
        int read;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while ((read = in.read(buffer, 0, bufferLength)) != -1) {
            out.write(buffer, 0, read);
        }
        return out.toByteArray();
    }
