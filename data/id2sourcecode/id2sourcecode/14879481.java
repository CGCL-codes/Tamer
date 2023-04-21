    public static byte[] readFileToByteArray(File src) throws IOException {
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(src));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int count = 0;
        while ((count = is.read(buf, 0, 1024)) != -1) os.write(buf, 0, count);
        is.close();
        os.close();
        return os.toByteArray();
    }
