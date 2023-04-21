    private void initIn(final InputStream in) {
        this.in = in;
        if (in instanceof XInputStream) {
            this.xin = (XInputStream) in;
            this.inChannel = this.xin;
        } else if (in instanceof ReadableByteChannel) this.inChannel = (ReadableByteChannel) in; else if (in instanceof FileInputStream) this.inChannel = ((FileInputStream) in).getChannel();
    }
