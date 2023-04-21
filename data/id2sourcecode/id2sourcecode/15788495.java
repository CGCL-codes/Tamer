    public static void saveStreamToFile(InputStream stream, File file) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        byte[] b = new byte[1024];
        int n;
        while ((n = stream.read(b)) > 0) out.write(b, 0, n);
        out.close();
    }
