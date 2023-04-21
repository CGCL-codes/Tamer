    public static byte[] getContentAndWrite(URLConnection con, ProxyRunData data) throws IOException {
        int CAPACITY = 4096;
        InputStream is = con.getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] bytes = new byte[CAPACITY];
        int readCount = 0;
        while ((readCount = is.read(bytes)) > 0) {
            buffer.write(bytes, 0, readCount);
            data.getResponse().getOutputStream().write(bytes, 0, readCount);
        }
        is.close();
        return buffer.toByteArray();
    }
