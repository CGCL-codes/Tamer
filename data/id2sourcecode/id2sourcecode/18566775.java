    protected WritableByteChannel getWriteChannel(final URL _url) throws IOException {
        WritableByteChannel channel;
        if (_url.getProtocol().equals("file")) {
            File file = null;
            if ((_url.getHost() != null) && !_url.getHost().equals("")) {
                file = new File(_url.getHost() + ':' + _url.getFile());
            } else {
                file = new File(_url.getFile());
            }
            final RandomAccessFile raf = new RandomAccessFile(file, "rw");
            channel = raf.getChannel();
            ((FileChannel) channel).lock();
        } else {
            final OutputStream out = _url.openConnection().getOutputStream();
            channel = Channels.newChannel(out);
        }
        return channel;
    }
