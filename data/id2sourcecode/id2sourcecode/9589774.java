    public static void writeFile(File src, OutputStream out) throws IOException {
        FileInputStream in = new FileInputStream(src);
        byte[] buffer = new byte[BUFFER_SIZE];
        int len = 0;
        while ((len = in.read(buffer)) > 0) out.write(buffer, 0, len);
        in.close();
        out.close();
    }
