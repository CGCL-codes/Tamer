    public static void urlToFile(URL url, File path) throws FileNotFoundException, IOException {
        java.io.BufferedInputStream in = new java.io.BufferedInputStream(url.openStream());
        java.io.FileOutputStream fos = new java.io.FileOutputStream(path);
        java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
        byte[] data = new byte[1024];
        int x = 0;
        while ((x = in.read(data, 0, 1024)) >= 0) {
            bout.write(data, 0, x);
        }
        bout.close();
        in.close();
    }
