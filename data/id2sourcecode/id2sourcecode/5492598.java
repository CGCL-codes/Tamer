    private InputStream debug(InputStream inputStream) throws IOException {
        long time = System.currentTimeMillis();
        ByteArrayOutputStream out = new ByteArrayOutputStream(8 * 1024);
        byte[] temp = new byte[2048];
        try {
            int read = inputStream.read(temp);
            while (read > 0) {
                out.write(temp, 0, read);
                read = inputStream.read(temp);
            }
        } finally {
            System.out.println("Took " + (System.currentTimeMillis() - time) + " millis");
        }
        return new ByteArrayInputStream(out.toByteArray());
    }
