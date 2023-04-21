    public static String readContent(InputStream content) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int readCount = content.read(buf); readCount > 0; readCount = content.read(buf)) {
                out.write(buf, 0, readCount);
            }
            return Util.toString(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            Utility.close(content);
        }
    }
