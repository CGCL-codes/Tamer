    private void writeTdsXVARBINARYWithKnownLength(final TdsParamInfo pi, final InputStream in) throws IOException, SQLException {
        assert getTdsVersion().isYukonOrLater();
        writeShort(-1);
        writeLong(pi.length);
        writeInt(pi.length);
        transferFrom(in, pi.length);
        writeInt(0);
    }
