    @Override
    public synchronized void write2Output(OutputData data) throws Exception {
        Map<String, OPCItemData> itemDataMap = data.itemData;
        for (Entry<String, PstmtItemInfo> e : updaterMap.entrySet()) {
            e.getValue().updateVal(itemDataMap.get(e.getKey()));
        }
        Map<String, ScriptDataResult> compData = data.compData;
        for (Entry<CompositeItem, PsmtCompositeUpdater> e : updaterCompMap.entrySet()) {
            e.getValue().updateVal(compData.get(e.getKey().getName()));
        }
        try {
            for (TableData t : tables) {
                t.getStmt().executeUpdate();
            }
            database.getCon().commit();
        } catch (SQLException ex) {
            database.getCon().rollback();
            throw ex;
        }
    }
