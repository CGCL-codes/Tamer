    public Leitor(URL url) {
        try {
            URLConnection c = url.openConnection();
            bytebuffer = ByteBuffer.allocate((int) c.getContentLength());
            InputStream in = new BufferedInputStream(c.getInputStream());
            ReadableByteChannel fc = Channels.newChannel(in);
            fc.read(bytebuffer);
            bytebuffer.position(0);
            fc.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
