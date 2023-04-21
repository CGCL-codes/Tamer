    public static int write(OutputStream out, ByteBuffer data) throws IOException {
        WritableByteChannel ch;
        if (out instanceof WritableByteChannel) ch = (WritableByteChannel) out; else if (out instanceof FileOutputStream) ch = ((FileOutputStream) out).getChannel(); else ch = null;
        if (ch != null) {
            int count = data.remaining();
            while (data.hasRemaining()) ch.write(data);
            return count;
        } else return IOImpl.write(out, data);
    }
