    StreamDecoder(InputStream in, Object lock, CharsetDecoder dec) {
        super(lock);
        this.cs = dec.charset();
        this.decoder = dec;
        if (false && in instanceof FileInputStream) {
            ch = getChannel((FileInputStream) in);
            if (ch != null) bb = ByteBuffer.allocateDirect(DEFAULT_BYTE_BUFFER_SIZE);
        }
        if (ch == null) {
            this.in = in;
            this.ch = null;
            bb = ByteBuffer.allocate(DEFAULT_BYTE_BUFFER_SIZE);
        }
        bb.flip();
    }
