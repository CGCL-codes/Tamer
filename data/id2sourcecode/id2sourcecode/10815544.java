    public ZipperInputStream(InputStream streamToZip) throws IOException {
        this.streamToZip = streamToZip;
        pipeIn = new CorePipedInputStream();
        pipeOut = new CorePipedOutputStream(pipeIn);
        zos = new ZipOutputStream(pipeOut);
        zos.putNextEntry(new ZipEntry("content"));
        array = new byte[1024];
    }
