    public static void setClob(java.sql.PreparedStatement stmt, int index, java.io.Reader input) throws SQLException {
        if (!PrismsUtils.isJava6()) throw new SQLException("Cannot set CLOB in <JDK 6 machine");
        if (input == null) stmt.setNull(index, java.sql.Types.CLOB); else {
            java.sql.Clob clob = stmt.getConnection().createClob();
            java.io.Writer stream = clob.setCharacterStream(1);
            try {
                int read = input.read();
                while (read >= 0) {
                    stream.write(read);
                    read = input.read();
                }
                stream.close();
            } catch (java.io.IOException e) {
                throw new SQLException(e.getMessage(), e);
            }
            stmt.setClob(index, clob);
        }
    }
