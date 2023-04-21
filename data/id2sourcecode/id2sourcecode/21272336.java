    public final long writeBlob(final InputStream in) throws IOException {
        if (in == null) {
            writeInt8Array(null);
            return 0;
        } else {
            Output out = writeType(PDS.TYPE_BLOB);
            out.writeInt64Bits(-1);
            out.beginBlock();
            try {
                long count = out.transferFrom(in, -1);
                out.endBlock();
                return count;
            } catch (IOException ex) {
                try {
                    out.endBlock();
                } finally {
                    throw ex;
                }
            }
        }
    }
