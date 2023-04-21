    private void initOut(final OutputStream out) {
        this.out = out;
        if (out instanceof WritableByteChannel) this.outChannel = (WritableByteChannel) out; else if (out instanceof FileOutputStream) this.outChannel = ((FileOutputStream) out).getChannel(); else this.outChannel = null;
    }
