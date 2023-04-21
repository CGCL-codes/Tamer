    public static String getString(Clob aClob) throws SQLException {
        Reader reader = aClob.getCharacterStream();
        SdlUnsynchronizedCharArrayWriter writer = new SdlUnsynchronizedCharArrayWriter();
        try {
            char[] buff = new char[1024 * 128];
            int read;
            while ((read = reader.read(buff)) != -1) {
                writer.write(buff, 0, read);
            }
            return writer.toString();
        } catch (IOException io) {
            SdlException.logError(io, "Error reading from clob");
            throw new SQLException("Error reading from clob" + ":" + io.getLocalizedMessage());
        } finally {
            SdlCloser.close(reader);
        }
    }
