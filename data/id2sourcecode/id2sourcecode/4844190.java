    public static void write(InputStream in, File f) throws IOException {
        FileOutputStream out = new FileOutputStream(f);
        int read = 0;
        byte[] buff = new byte[1024];
        while ((read = in.read(buff)) > 0) {
            out.write(buff, 0, read);
        }
        in.close();
        out.close();
    }
