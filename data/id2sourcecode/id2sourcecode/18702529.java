    public boolean cleanAllTablesData() {
        Statement tStat = getStatement();
        setAutoCommit(false);
        try {
            List<String> tAllTables = getInsertOrder();
            String tDelete = null;
            for (int i = tAllTables.size() - 1; i >= 0; i--) {
                tDelete = "Delete From ".intern() + tAllTables.get(i).intern();
                executeUpdate(tStat, tDelete);
            }
            commit();
            return true;
        } catch (Exception e) {
            rollback();
            LOG.error("", e);
        } finally {
            close(tStat);
            setAutoCommit(true);
        }
        return false;
    }
