    public static long transfer(Readable in, Appendable out, long maxCount) throws IOException {
        CheckArg.maxCount(maxCount);
        if (maxCount == 0) return 0; else if (in instanceof XReader) return ((XReader) in).transferTo(out, maxCount); else if (out instanceof XWriter) return ((XWriter) out).transferFrom(in, maxCount); else if (in instanceof Reader) {
            Reader rin = (Reader) in;
            long count = 0;
            for (int b; (count != maxCount) && ((b = rin.read()) >= 0); ) {
                out.append((char) b);
                count++;
            }
            return count;
        } else if (in instanceof CharBuffer) {
            CharBuffer b = (CharBuffer) in;
            long count = (maxCount < 0) ? b.remaining() : Math.min(maxCount, b.remaining());
            if (count == b.remaining()) {
                out.append(b);
                b.position(b.limit());
            } else {
                CharBuffer c = b.slice();
                c.limit((int) count);
                out.append(c);
                b.position(b.position() + (int) count);
            }
            return count;
        } else {
            int bufSize = (maxCount < 0) ? 256 : (int) Math.min(256, maxCount);
            CharBuffer buf = CharBuffer.allocate(bufSize);
            long count = 0;
            while (count != maxCount) {
                int step = (maxCount < 0) ? bufSize : (int) Math.min(maxCount - count, bufSize);
                buf.position(0);
                buf.limit(step);
                int a = in.read(buf);
                if (a < 0) break; else if (a != buf.position()) throw new ReturnValueException(in, "read(CharBuffer)" + a + "== buf.position()"); else if (a > step) throw new ReturnValueException(in, "read(CharBuffer)" + a + "<=" + step); else {
                    buf.flip();
                    out.append(buf);
                    count += a;
                }
            }
            return count;
        }
    }
