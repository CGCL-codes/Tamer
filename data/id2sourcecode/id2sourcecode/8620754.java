    private void writeTdsXVARBINARYSmall(final TdsParamInfo pi, final InputStream in) throws IOException, SQLException {
        writeShort(TdsTypes.MS_LONGVAR_MAX);
        writeShort(pi.length);
        transferFrom(in, pi.length);
    }
