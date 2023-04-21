    public CharWrapper(Clob clob) {
        try {
            Reader reader = clob.getCharacterStream();
            Writer writer = new StringWriter();
            IOUtils.pipe(reader, writer);
            this.str = writer.toString();
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }
