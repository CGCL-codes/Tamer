    public static String readText(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] buffer = new byte[1024];
        while (true) {
            int read = bis.read(buffer);
            if (read < 0) {
                break;
            }
            baos.write(buffer, 0, read);
        }
        byte[] bytes = baos.toByteArray();
        return new String(bytes);
    }
