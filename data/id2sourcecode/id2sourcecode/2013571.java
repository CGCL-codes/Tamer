    private static void writeFile(InputStream is, File file) throws IOException {
        if (file.exists()) return;
        System.out.println("unpacking " + file.getPath());
        FileOutputStream fos = new FileOutputStream(file);
        byte[] data = new byte[100000];
        int read = is.read(data);
        while (read > 0) {
            fos.write(data, 0, read);
            read = is.read(data);
        }
        fos.close();
    }
