    @Override
    public int read(final ByteBuffer dest) throws IOException {
        final int count = dest.remaining();
        if (count > 0) {
            long remaining = this.remaining;
            if (remaining > 0) {
                final InputStream in = this.in;
                final ReadableByteChannel inChannel;
                if (in instanceof ReadableByteChannel) inChannel = (ReadableByteChannel) in; else if (in instanceof FileInputStream) inChannel = ((FileInputStream) in).getChannel(); else return super.read(dest);
                ByteBuffer dupe = null;
                int readed = 0;
                while (true) {
                    final int maxStep = (int) Math.min(remaining, count - readed);
                    if (maxStep > 0) {
                        final ByteBuffer buf;
                        if (count - readed >= maxStep) {
                            dupe = null;
                            buf = dest;
                        } else {
                            if (dupe == null) dupe = dest.duplicate();
                            dupe.limit(dest.position() + maxStep);
                            buf = dupe;
                        }
                        final int step = inChannel.read(buf);
                        if (step > 0) {
                            if (buf != dest) dest.position(buf.position());
                            if (step <= maxStep) {
                                this.remaining = remaining -= step;
                                readed += step;
                                if (step == remaining) {
                                    eof();
                                    break;
                                } else {
                                    continue;
                                }
                            } else {
                                throw new ReturnValueException(inChannel, "read(ByteBuffer)", step, "<=", maxStep);
                            }
                        } else if (step == 0) {
                            return readed + super.read(dest);
                        } else {
                            throw new IOException("stream truncated");
                        }
                    } else {
                        break;
                    }
                }
                return readed;
            } else {
                return -1;
            }
        } else {
            return 0;
        }
    }
