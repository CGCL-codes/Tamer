    private void writeTdsIMAGE(final TdsParamInfo pi) throws IOException, SQLException {
        final int len = (pi.value == null) ? 0 : pi.length;
        writeByte(TdsTypes.SYBIMAGE);
        if (len > 0) {
            InputStream in;
            if (pi.value instanceof InputStream) in = (InputStream) pi.value; else if (pi.value instanceof Blob) in = ((Blob) pi.value).getBinaryStream(); else in = null;
            if (in != null) {
                pi.value = null;
                writeInt(len);
                writeInt(len);
                try {
                    transferFrom(in, len);
                    in.close();
                    in = null;
                } finally {
                    IO.tryClose(in);
                }
            } else {
                final ByteBuffer buf = pi.getOutputBytes(this);
                pi.value = null;
                writeInt(buf.remaining());
                writeInt(buf.remaining());
                write(buf);
            }
        } else if (getTdsVersion().major < 7) {
            writeInt(1);
            writeInt(1);
            writeByte(0);
        } else {
            writeInt(0);
            writeInt(0);
        }
    }
