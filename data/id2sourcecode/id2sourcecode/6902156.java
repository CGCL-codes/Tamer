    public RowSetWriter getRowSetWriter() {
        try {
            writer.setReader(reader);
        } catch (java.sql.SQLException e) {
        }
        return writer;
    }
