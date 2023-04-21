    private byte[] readBlockFromStream(InputStream in) throws IOException {
        byte[] buffer = new byte[4096];
        int readed = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while ((readed = in.read(buffer)) != -1) out.write(buffer, 0, readed);
        return out.toByteArray();
    }
