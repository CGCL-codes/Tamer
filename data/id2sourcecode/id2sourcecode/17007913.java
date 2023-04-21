    public OutputStreamXWriter(final OutputStream out, final CharsetEncoder encoder) {
        super();
        if (out == null) throw new NullPointerException("out");
        if (encoder == null) throw new NullPointerException("encoder");
        this.out = out;
        this.encoder = encoder;
        this.maxBytesPerChar = Ints.max(2, (int) Math.ceil(encoder.maxBytesPerChar()), encoder.replacement().length);
        setByteBufferCapacity0(DEFAULT_OUTPUT_BUFFER_CAPACITY);
        if (out instanceof FileOutputStream) this.outChannel = ((FileOutputStream) out).getChannel(); else if (out instanceof WritableByteChannel) this.outChannel = (WritableByteChannel) out;
    }
