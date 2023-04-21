    private static byte[] getByteBuffer(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read = 0;
        while ((read = in.read()) != -1) {
            out.write(read);
        }
        byte[] bytes = out.toByteArray();
        out.close();
        out.flush();
        return bytes;
    }
