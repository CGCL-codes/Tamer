    public long transferFrom(Readable in, long maxCount) throws IOException {
        CheckArg.maxCount(maxCount);
        if (maxCount == 0) return 0;
        long count = 0;
        if (in instanceof Reader) {
            Reader r = (Reader) in;
            while ((count < maxCount) || (maxCount < 0)) {
                int b = r.read();
                if (b < 0) {
                    break;
                } else {
                    write(b);
                    count++;
                }
            }
        } else {
            int bufSize = (maxCount < 0) ? 256 : (int) Math.min(maxCount, 256);
            CharBuffer buf = CharBuffer.allocate(256);
            while (((count < maxCount) || (maxCount < 0)) && (in.read(buf) >= 0)) {
                buf.flip();
                count += buf.position();
                write(buf.array(), 0, buf.position());
                buf.clear();
            }
        }
        return count;
    }
