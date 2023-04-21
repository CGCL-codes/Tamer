    public void fetchIntoStream(String fileNameKey, int part, int batchSize, OutputStream out) throws IOException {
        File doc = new File(fileNameKey + "_" + part);
        if (doc.exists() && doc.isFile()) {
            FileInputStream input = new FileInputStream(doc);
            BufferedInputStream buf = new BufferedInputStream(input);
            int readBytes = 0;
            while ((readBytes = buf.read()) != -1) {
                out.write(readBytes);
            }
            if (buf != null) {
                buf.close();
            }
        }
    }
