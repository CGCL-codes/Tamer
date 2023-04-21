    public final long writeBlob(final InputStream in, long length) throws IOException {
        if (length < 0) {
            return writeBlob(in);
        } else if (in == null) {
            if (length > 0) throw new IllegalArgumentException("in == null but length > 0: " + length);
            writeInt8Array(null);
            return 0;
        } else if (length <= Integer.MAX_VALUE) {
            writeInt8Array(in, (int) length);
            return length;
        } else {
            Output out = writeType(PDS.TYPE_BLOB);
            out.writeInt64Bits(length);
            long count = out.transferFrom(in, length);
            if (count == length) return count; else if (count < length) throw new EOFException(); else throw new AssertionError();
        }
    }
