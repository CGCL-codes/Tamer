    public static void writeFileContentToStream(File file, OutputStream out) throws Exception {
        FileInputStream fw = new FileInputStream(file);
        byte[] block = new byte[10000];
        int read = 0;
        while ((read = fw.read(block)) > 0) {
            out.write(block, 0, read);
        }
        fw.close();
    }
