    private int mergeTable(String TableName, String ColumnName, int from_ID, int to_ID) {
        log.fine(TableName + "." + ColumnName + " - From=" + from_ID + ",To=" + to_ID);
        String sql = "UPDATE " + TableName + " SET " + ColumnName + "=" + to_ID + " WHERE " + ColumnName + "=" + from_ID;
        boolean delete = false;
        for (int i = 0; i < m_deleteTables.length; i++) {
            if (m_deleteTables[i].equals(TableName)) {
                delete = true;
                sql = "DELETE " + TableName + " WHERE " + ColumnName + "=" + from_ID;
            }
        }
        if (delete && X_M_Cost.Table_Name.equals(TableName) && M_PRODUCT_ID.equals(ColumnName)) {
            sql += " AND CurrentCostPrice =0" + " AND CurrentQty =0" + " AND CumulatedAmt=0" + " AND CumulatedQty=0";
        }
        int count = org.compierezk.util.DB.executeUpdate(sql, m_trx);
        if (count < 0) {
            count = -1;
            m_errorLog.append(Env.NL).append(delete ? "DELETE " : "UPDATE ").append(TableName).append(" - ").append(" - ").append(sql);
            log.config(m_errorLog.toString());
            m_trx.rollback();
        }
        log.fine(count + (delete ? " -Delete- " : " -Update- ") + TableName);
        return count;
    }
