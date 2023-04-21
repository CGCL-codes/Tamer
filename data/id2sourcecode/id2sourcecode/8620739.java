    private void writeTdsTEXT(final TdsParamInfo pi) throws IOException, SQLException {
        int len;
        if (pi.value == null) len = 0; else {
            len = pi.length;
            if ((len == 0) && (getTdsVersion().major < 7)) {
                pi.value = " ";
                len = 1;
            }
        }
        writeByte(TdsTypes.SYBTEXT);
        if (len > 0) {
            InputStream in;
            if (pi.value instanceof InputStream) in = (InputStream) pi.value; else if (pi.value instanceof Blob) in = ((Blob) pi.value).getBinaryStream(); else in = null;
            if (in != null) {
                pi.value = null;
                try {
                    writeInt(len);
                    if (getTdsVersion().major >= 8) writeTdsCollation(pi);
                    writeInt(len);
                    transferFrom(in, len);
                    in.close();
                    in = null;
                } finally {
                    IO.tryClose(in);
                }
            } else if ((pi.value instanceof Readable) && !pi.charsetInfo.wideChars) {
                Readable reader = (Reader) pi.value;
                try {
                    pi.value = null;
                    writeInt(len);
                    if (getTdsVersion().major >= 8) writeTdsCollation(pi);
                    writeInt(len);
                    transferCharsFromEncoded(reader, len);
                    IO.closeIfCloseable(reader);
                    reader = null;
                } finally {
                    IO.tryCloseIfCloseable(reader);
                }
            } else {
                final ByteBuffer buf = pi.getOutputBytes(this);
                pi.value = null;
                writeInt(buf.remaining());
                if (getTdsVersion().major >= 8) writeTdsCollation(pi);
                writeInt(buf.remaining());
                write(buf);
            }
        } else {
            writeInt(len);
            if (getTdsVersion().major >= 8) writeTdsCollation(pi);
            writeInt(len);
        }
    }
