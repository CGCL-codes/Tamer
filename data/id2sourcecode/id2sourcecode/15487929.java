    public ByteArrayDataSource(InputStream is, String type) throws IOException {
        DSByteArrayOutputStream os = new DSByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int len;
        while ((len = is.read(buf)) > 0) os.write(buf, 0, len);
        this.data = os.getBuf();
        this.len = os.getCount();
        if (this.data.length - this.len > 256 * 1024) {
            this.data = os.toByteArray();
            this.len = this.data.length;
        }
        this.type = type;
    }
