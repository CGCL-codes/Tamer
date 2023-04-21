    public static byte[] getResource(String name, Object target) throws IOException {
        Class cl = (target instanceof Class) ? (Class) target : target.getClass();
        InputStream is = cl.getResourceAsStream(name);
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int pos = 0;
        int readed;
        final int chunk_size = 4096;
        byte[] buf = new byte[chunk_size];
        while (-1 != (readed = bis.read(buf, 0, chunk_size))) {
            baos.write(buf, pos, readed);
            pos += readed;
        }
        return baos.toByteArray();
    }
