    public static byte[] readFile(InputStream inputStream, int fileLength) throws IOException {
        ByteArrayOutputStream bos = null;
        bos = new ByteArrayOutputStream();
        int elapsed = 0;
        int read = 0;
        byte[] bytes = new byte[BUF_SIZE];
        while (elapsed < fileLength) {
            read = inputStream.read(bytes);
            bos.write(bytes, 0, read);
            if (read < 0) {
                break;
            } else if (read == 0) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
            elapsed += read;
        }
        return bos.toByteArray();
    }
