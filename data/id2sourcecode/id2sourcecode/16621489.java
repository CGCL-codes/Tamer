    public static void copyURLToFile(URL url, String filename) throws IOException {
        byte[] buf = new byte[1];
        InputStream in = url.openStream();
        FileOutputStream out = new FileOutputStream(filename);
        while ((in.read(buf, 0, 1)) != -1) {
            out.write(buf, 0, 1);
        }
        in.close();
        out.close();
    }
