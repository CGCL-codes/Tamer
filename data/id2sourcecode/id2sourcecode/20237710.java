    public static void setBlob(java.sql.PreparedStatement stmt, int index, java.io.InputStream input) throws SQLException {
        if (!PrismsUtils.isJava6()) throw new SQLException("Cannot set binary data in <JDK 6 machine");
        if (input == null) stmt.setNull(index, java.sql.Types.BLOB); else {
            java.sql.Blob blob = stmt.getConnection().createBlob();
            java.io.OutputStream stream = blob.setBinaryStream(1);
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
            stmt.setBlob(index, blob);
        }
    }
