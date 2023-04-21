    private StreamEncoder(OutputStream out, Object lock, CharsetEncoder enc) {
        super(lock);
        this.out = out;
        this.ch = null;
        this.cs = enc.charset();
        this.encoder = enc;
        if (false && out instanceof FileOutputStream) {
            ch = ((FileOutputStream) out).getChannel();
            if (ch != null) bb = ByteBuffer.allocateDirect(DEFAULT_BYTE_BUFFER_SIZE);
        }
        if (ch == null) {
            bb = ByteBuffer.allocate(DEFAULT_BYTE_BUFFER_SIZE);
        }
    }
