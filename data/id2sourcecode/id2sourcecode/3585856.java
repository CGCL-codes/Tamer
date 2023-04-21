    @Override
    public int read(final ByteBuffer dest) throws IOException {
        int remaining = dest.remaining();
        if (remaining <= 0) return 0;
        final InputStream in = this.in;
        final ReadableByteChannel inChannel;
        if (in instanceof ReadableByteChannel) inChannel = (ReadableByteChannel) in; else if (in instanceof FileInputStream) inChannel = ((FileInputStream) in).getChannel(); else return super.read(dest);
        CheckArg.writable(dest);
        int remainingInBlock = remainingInBlock();
        int count = 0;
        if (remainingInBlock > 0) {
            ByteBuffer dupe = null;
            while (true) {
                final int maxStep = Math.min(remaining, remainingInBlock);
                if (maxStep <= 0) break;
                final ByteBuffer buf;
                if (remaining >= maxStep) buf = dest; else {
                    if (dupe == null) dupe = dest.duplicate();
                    dupe.limit(dest.position() + maxStep);
                    buf = dupe;
                }
                final int step = inChannel.read(buf);
                if (step > 0) {
                    if (buf != dest) dest.position(buf.position());
                    if (step > maxStep) throw new ReturnValueException(inChannel, "read(ByteBuffer)", step, "<=", maxStep);
                    this.remainingInBlock = remainingInBlock - step;
                    remaining -= step;
                    count += step;
                    if (remaining <= 0) break;
                    remainingInBlock = remainingInBlock();
                    continue;
                } else if (step == 0) return count + super.read(dest); else break;
            }
        }
        return (count > 0) ? count : -1;
    }
