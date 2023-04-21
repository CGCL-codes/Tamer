    public Object db2java(ResultSet set, int i, int sqlType, boolean createLong) throws SQLException {
        Object obj = super.db2java(set, i, sqlType, createLong);
        if (sqlType != Types.OTHER) return obj;
        boolean isClob = obj instanceof Clob;
        boolean isBlob = isClob ? false : (obj instanceof Blob);
        if (isClob || (obj instanceof CLOB)) {
            Clob clob = isClob ? (Clob) obj : null;
            CLOB _clob = isClob ? null : (CLOB) obj;
            int clen = (int) (isClob ? clob.length() : _clob.length()) + 1;
            StringWriter writer = new StringWriter(clen);
            if (!readAndWrite(isClob ? clob.getCharacterStream() : _clob.getCharacterStream(), writer, clen)) return null; else return writer.getBuffer();
        } else if (isBlob || (obj instanceof BLOB)) {
            Blob blob = isBlob ? (Blob) obj : null;
            BLOB _blob = isBlob ? (BLOB) obj : null;
            int blen = (int) (isBlob ? blob.length() : _blob.length()) + 1;
            ByteArrayOutputStream output = new ByteArrayOutputStream(blen);
            if (!readAndWrite(isBlob ? blob.getBinaryStream() : _blob.getBinaryStream(), output, blen)) return null; else return output.toByteArray();
        }
        return obj;
    }
