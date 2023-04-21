    public static void updateDictionaryColumn(String tableName, String columnName) {
        DefTable defTable = BLSession.getDictionary().getDefTables().getDefTable(tableName);
        String connectionName = defTable.getConnectionName();
        if ((connectionName == null) || (connectionName.trim().equals("") == true)) {
            connectionName = DLSession.getConnection().getDefaultConnectionName();
        }
        DLSession.getTransaction().begin();
        try {
            RecordSet rst = DLSession.getConnection().executeQuery("(" + connectionName + ")SELECT " + columnName + " FROM " + tableName + " WHERE 1=0");
            Parameters parameters = new Parameters();
            parameters.addParameter(rst.getDataType(columnName).ordinal(), DataType.DT_INT);
            parameters.addParameter(tableName, DataType.DT_STRING);
            parameters.addParameter(columnName, DataType.DT_STRING);
            DLSession.getConnection().executeUpdate("(DICTIONARY)UPDATE BL_DefColumn SET DataType=? WHERE TableName=? AND ColumnName=?", parameters);
            rst.close();
        } catch (RuntimeException rex) {
            DLSession.getTransaction().rollback();
            throw rex;
        } catch (Exception ex) {
            DLSession.getTransaction().rollback();
            throw new RuntimeException(ex);
        }
        DLSession.getTransaction().commit();
    }
