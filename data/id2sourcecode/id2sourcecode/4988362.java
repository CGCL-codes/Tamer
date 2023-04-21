    public void readFilteredRelatedData(TableMetaData tableMetaData, List<String> tablesRead, String referencedBy, String filter, List<Row> relatedData, boolean imported) throws Exception {
        boolean initialTable = false;
        boolean readImportsAgain = false;
        if (tablesRead == null) {
            tablesRead = new ArrayList<String>(0);
            DBUtil.readTableData(tableMetaData, filter);
            tablesRead.add(tableMetaData.getTableName());
        } else {
            if (!tablesRead.contains(tableMetaData.getTableName() + referencedBy)) {
                DBUtil.readTableDataByReference(tableMetaData, referencedBy, relatedData, imported);
                if (tableMetaData.getData().size() > 0) {
                    tablesRead.add(tableMetaData.getTableName() + referencedBy);
                    readImportsAgain = true;
                }
            }
        }
        for (TableMetaData refTableMetaData : tableMetaData.getReferencesImported()) {
            if ((!tablesRead.contains(refTableMetaData.getTableName() + tableMetaData.getTableName()) || readImportsAgain) && !refTableMetaData.getTableName().equalsIgnoreCase(referencedBy)) {
                IOHelper.writeInfo("Reading reference data on " + refTableMetaData.getTableName() + " from " + tableMetaData.getTableName());
                readFilteredRelatedData(refTableMetaData, tablesRead, tableMetaData.getTableName(), filter, tableMetaData.getData(), true);
            } else if ("advogado_processo_origem".equals(tableMetaData.getTableName())) {
                IOHelper.writeInfo("Reading reference data on " + refTableMetaData.getTableName() + " from " + tableMetaData.getTableName() + "already done");
            }
        }
        for (TableMetaData refTableMetaData : tableMetaData.getReferencesExported()) {
            if (!tablesRead.contains(refTableMetaData.getTableName() + tableMetaData.getTableName()) && !refTableMetaData.getTableName().equalsIgnoreCase(referencedBy)) {
                IOHelper.writeInfo("Reading reference data on " + refTableMetaData.getTableName() + " from " + tableMetaData.getTableName());
                readFilteredRelatedData(refTableMetaData, tablesRead, tableMetaData.getTableName(), filter, tableMetaData.getData(), false);
            } else if ("advogado_processo_origem".equals(tableMetaData.getTableName())) {
                IOHelper.writeInfo("Reading reference data on " + refTableMetaData.getTableName() + " from " + tableMetaData.getTableName() + "already done");
            }
        }
    }
