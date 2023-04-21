    @Override
    public void write(byte[] data, FileStorageMetadata metadata) throws IOException {
        FileOutputStream os = new FileOutputStream(file);
        try {
            InputStream is = new ByteArrayInputStream(data);
            int length = 4096;
            byte[] buf = new byte[length];
            int readed = 0;
            while ((readed = is.read(buf, 0, buf.length)) != -1) {
                os.write(buf, 0, readed);
            }
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
