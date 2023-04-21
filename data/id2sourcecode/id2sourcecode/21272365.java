    public final void writeInt8Array(final InputStream in, final int length) throws IOException {
        CheckArg.notNegative(length, "length");
        final Output out = writeType(PDS.TYPE_ARRAY_INT8);
        if (in == null) {
            out.writeNull();
        } else {
            out.writeInt32Bits(length);
            if (out.transferFrom(in, length) < length) throw new EOFException();
        }
    }
