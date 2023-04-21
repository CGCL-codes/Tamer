    public static void copyResource(String source, File destination) throws Exception {
        System.out.println("source: " + source);
        InputStream in = getResourceAsStream(source);
        OutputStream out = new FileOutputStream(destination);
        byte[] buffer = new byte[32 * 1024];
        int len;
        while ((len = in.read(buffer)) != -1) out.write(buffer, 0, len);
        out.close();
    }
