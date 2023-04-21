    public Object handle(ResultSet rs) throws SQLException {
        Reader input = null;
        if (rs.next()) {
            mCounter = rs.getInt(2);
            Reader inFromClob = rs.getClob(1).getCharacterStream();
            char[] buffer = new char[1024 * 4];
            int read;
            StringWriter writer = new StringWriter();
            try {
                while ((read = inFromClob.read(buffer)) != -1) {
                    writer.write(buffer, 0, read);
                }
                input = new StringReader(writer.toString());
            } catch (IOException e) {
                throw new SQLException(e.getMessage());
            } finally {
                SdlCloser.close(inFromClob);
            }
        }
        return input;
    }
