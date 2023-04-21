    public String getContentAsString() throws IOException {
        InputStream in = getContent();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] bytes = new byte[4 * 1024];
        int read = in.read(bytes);
        while (read != -1) {
            out.write(bytes, 0, read);
            read = in.read(bytes);
        }
        in.close();
        return new String(out.toByteArray());
    }
