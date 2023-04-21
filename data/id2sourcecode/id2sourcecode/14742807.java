    public static byte[] readJarFile(String file) throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        InputStream in = StealthnetServer.class.getResourceAsStream(file);
        byte[] block = new byte[5000];
        int read;
        while ((read = in.read(block)) > -1) {
            bout.write(block, 0, read);
        }
        in.close();
        return bout.toByteArray();
    }
