    private void copy(InputStream fis, String outFile) throws Exception {
        FileOutputStream fos = new FileOutputStream(new File(outFile));
        byte[] data = new byte[100000];
        int read = fis.read(data);
        while (read > 0) {
            fos.write(data, 0, read);
            read = fis.read(data);
        }
        fos.close();
        fis.close();
    }
