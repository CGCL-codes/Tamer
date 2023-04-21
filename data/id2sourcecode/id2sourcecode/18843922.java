    private int refreshBlockList(PrintWriter page, List<String> directoryList, ConfigManager settings, String ownerOrganisation, long groupPrimary, String accessDelete, String accessModify, String accessRead, String accessWrite) throws DataAccessException, SQLException {
        DataAccessor da = new DataAccessor();
        Connection conn = null;
        Statement stmt;
        String tableBlockList;
        ResultSet dataResult;
        Iterator<String> entries;
        String eachEntry;
        String blockEntry;
        int refreshedBlocksCount;
        try {
            conn = da.getConnection();
            stmt = da.getStatement(conn);
            refreshedBlocksCount = 0;
            tableBlockList = settings.getDefaultBlockPermissionsTableName();
            for (entries = directoryList.iterator(); entries.hasNext(); ) {
                eachEntry = (String) entries.next();
                blockEntry = eachEntry.substring(0, eachEntry.indexOf(".xml"));
                lastSQL = "SELECT \"primary\" FROM " + tableBlockList + " WHERE \"block\" = '" + blockEntry + "' AND \"ownerorganisation\" = '" + ownerOrganisation + "' AND \"fkgroup\" = " + groupPrimary + ";";
                dataResult = da.executeQueryAsync(stmt, lastSQL);
                boolean hasBlock = false;
                while (dataResult.next()) {
                    hasBlock = true;
                }
                if (hasBlock == false) {
                    lastSQL = "INSERT INTO " + tableBlockList + "(\"block\", \"read\", \"write\", \"delete\", \"modify\", \"owneror" + "ganisation\", \"fkgroup\") VALUES ('" + blockEntry + "', " + accessRead + ", " + accessWrite + ", " + accessDelete + ", " + accessModify + ", '" + ownerOrganisation + "', " + groupPrimary + ");";
                    da.executeUpdateAsync(stmt, lastSQL);
                    refreshedBlocksCount++;
                }
            }
            return refreshedBlocksCount;
        } finally {
            da.closeConnection(conn);
        }
    }
