    public static int write(OutputStream out, ByteBuffer data) throws IOException {
        final int count = data.remaining();
        if (count == 0) {
            return 0;
        } else if (data.hasArray()) {
            out.write(data.array(), data.arrayOffset() + data.position(), count);
            data.position(data.limit());
        } else {
            WritableByteChannel ch;
            if (out instanceof WritableByteChannel) ch = (WritableByteChannel) out; else if (out instanceof FileOutputStream) ch = ((FileOutputStream) out).getChannel(); else ch = null;
            if (ch == null) {
                for (int r = count; --r >= 0; ) out.write(data.get());
            } else {
                for (int r = count; r > 0; ) {
                    int step = ch.write(data);
                    if ((step < 0) || (step > r)) throw new ReturnValueException(ch, "write(ByteBuffer)" + step, ">= 0 && <=" + r);
                    r -= step;
                }
            }
        }
        return count;
    }
