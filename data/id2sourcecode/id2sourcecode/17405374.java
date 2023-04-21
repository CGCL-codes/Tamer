    public static String readContent(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte data[] = new byte[2048];
        int nbread = 0;
        while ((nbread = is.read(data)) > 0) {
            baos.write(data, 0, nbread);
        }
        return baos.toString();
    }
