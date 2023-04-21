    protected void setReader(int i, Reader reader, PreparedStatement stmt) throws SQLException {
        StringWriter writer = new StringWriter();
        IOUtils.pipe(reader, writer);
        String str = writer.toString();
        stmt.setCharacterStream(i, new StringReader(str), str.length());
    }
