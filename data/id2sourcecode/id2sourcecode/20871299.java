    public static void generateDictionaryColumn(String tableName, String columnName) {
        DefTable defTable = BLSession.getDictionary().getDefTables().getDefTable(tableName);
        String connectionName = defTable.getConnectionName();
        if ((connectionName == null) || (connectionName.trim().equals("") == true)) {
            connectionName = DLSession.getConnection().getDefaultConnectionName();
        }
        DLSession.getTransaction().begin();
        try {
            RecordSet rst = DLSession.getConnection().executeQuery("(" + connectionName + ")SELECT " + columnName + " FROM " + tableName + " WHERE 1=0");
            rst.next();
            RecordSet rstMax = DLSession.getConnection().executeQuery("(DICTIONARY)SELECT MAX(ColumnIndex) FROM BL_DefColumn WHERE TableName=?", tableName, DataType.DT_STRING);
            rstMax.next();
            int columnIndex = rstMax.getInt(0) + 1;
            if (rstMax.wasNull() == true) {
                columnIndex = 0;
            }
            Parameters parameters = new Parameters();
            parameters.addParameter(tableName, DataType.DT_STRING);
            parameters.addParameter(columnName, DataType.DT_STRING);
            parameters.addParameter(columnIndex, DataType.DT_INT);
            parameters.addParameter(rst.getDataType(columnName).ordinal(), DataType.DT_INT);
            DLSession.getConnection().executeUpdate("(DICTIONARY)INSERT INTO BL_DefColumn(TableName,ColumnName,ColumnIndex,DataType) VALUES (?,?,?,?)", parameters);
            rst.close();
            rstMax.close();
        } catch (RuntimeException rex) {
            DLSession.getTransaction().rollback();
            throw rex;
        } catch (Exception ex) {
            DLSession.getTransaction().rollback();
            throw new RuntimeException(ex);
        }
        DLSession.getTransaction().commit();
    }
