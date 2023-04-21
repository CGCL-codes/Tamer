    public Object handleRS(ResultSet rs) throws SQLException {
        if (rs.next()) {
            ResultSetMetaData rsMeta = rs.getMetaData();
            int columnType = rsMeta.getColumnType(1);
            if (columnType == Types.BINARY || columnType == Types.LONGVARBINARY) {
                Blob blob = rs.getBlob(1);
                if (blob != null) {
                    InputStream is = blob.getBinaryStream();
                    byte[] buffer = new byte[1024];
                    try {
                        while (is.read(buffer) != -1) {
                            this.outputStreamPtr.write(buffer);
                        }
                        is.close();
                    } catch (IOException e) {
                        throw new NullPointerException("It's impossible to read/write data: data is null");
                    }
                }
            } else {
                throw new IllegalArgumentException("The selected column is not a binary data");
            }
        }
        return null;
    }
