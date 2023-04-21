    private byte[] readImageFile() throws IOException {
        byte[] buf = new byte[4096];
        InputStream is = this.getClass().getResourceAsStream("thumbnail.png");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int read = 0;
        while ((read = is.read(buf)) > 0) {
            os.write(buf, 0, read);
        }
        is.close();
        return os.toByteArray();
    }
