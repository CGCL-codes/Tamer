    @Override
    protected DataElement getElement(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        String comment = rs.getString(2);
        int data_type_id = rs.getInt(3);
        String read_function = rs.getString(4);
        String write_function = rs.getString(5);
        return new ParseFormat(id, comment, data_type_id, read_function, write_function);
    }
