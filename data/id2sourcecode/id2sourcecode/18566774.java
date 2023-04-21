    protected ReadableByteChannel getReadChannel(final URL _url) throws IOException {
        ReadableByteChannel channel = null;
        if (_url.getProtocol().equals("file")) {
            File file = null;
            if ((_url.getHost() != null) && !_url.getHost().equals("")) {
                file = new File(_url.getHost() + ":" + _url.getFile());
            } else {
                file = new File(_url.getFile());
            }
            if (!file.exists() || !file.canRead()) {
                throw new IOException("File either doesn't exist or is unreadable : " + file);
            }
            final FileInputStream in = new FileInputStream(file);
            channel = in.getChannel();
        } else {
            final InputStream in = _url.openConnection().getInputStream();
            channel = Channels.newChannel(in);
        }
        return channel;
    }
