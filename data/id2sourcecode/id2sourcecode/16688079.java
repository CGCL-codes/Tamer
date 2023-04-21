    private int refreshTableList(PrintWriter page, List<String> allTables, ConfigManager settings, String ownerOrganisation, long groupPrimary, String accessDelete, String accessModify, String accessRead, String accessWrite) throws DataAccessException {
        DataAccessor da = new DataAccessor();
        Connection conn = null;
        Statement stmt;
        ResultSet dataResult;
        try {
            conn = da.getConnection();
            stmt = da.getStatement(conn);
            int refreshedTablesCount = 0;
            String tableTableList = settings.getDefaultTablePermissionsTableName();
            for (Iterator<String> entries = allTables.iterator(); entries.hasNext(); ) {
                String tableName = (String) entries.next();
                lastSQL = "SELECT \"primary\" FROM " + tableTableList + " WHERE \"table\" = '" + tableName + "' AND \"ownerorganisation\" = '" + ownerOrganisation + "' AND \"fkgroup\" = " + groupPrimary + ";";
                dataResult = da.executeQueryAsync(stmt, lastSQL);
                boolean hasTable = dataResult.next();
                if (!hasTable) {
                    lastSQL = "INSERT INTO " + tableTableList + "(\"table\", \"read\", \"write\", \"delete\", \"modify\", \"ownerorganisation\", " + "\"fkgroup\") VALUES ('" + tableName + "', " + accessRead + ", " + accessWrite + ", " + accessDelete + ", " + accessModify + ", '" + ownerOrganisation + "', " + groupPrimary + ");";
                    da.executeUpdateAsync(stmt, lastSQL);
                    refreshedTablesCount++;
                }
            }
            return refreshedTablesCount;
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        } finally {
            da.closeConnection(conn);
        }
    }
