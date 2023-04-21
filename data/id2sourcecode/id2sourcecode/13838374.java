    private boolean merge(String ColumnName, int from_ID, int to_ID) {
        String TableName = ColumnName.substring(0, ColumnName.length() - 3);
        log.config(ColumnName + " - From=" + from_ID + ",To=" + to_ID);
        boolean success = true;
        m_totalCount = 0;
        m_errorLog = new StringBuffer();
        String sql = "SELECT t.TableName, c.ColumnName " + "FROM AD_Table t" + " INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID) " + "WHERE t.IsView='N'" + " AND t.TableName NOT IN ('C_TaxDeclarationAcct')" + " AND (" + "(c.ColumnName=? AND c.IsKey='N')" + " OR " + "c.AD_Reference_Value_ID IN " + "(SELECT rt.AD_Reference_ID FROM AD_Ref_Table rt" + " INNER JOIN AD_Column cc ON (rt.AD_Table_ID=cc.AD_Table_ID AND rt.AD_Key=cc.AD_Column_ID) " + "WHERE cc.IsKey='Y' AND cc.ColumnName=?)" + ") " + "ORDER BY t.LoadSeq DESC";
        PreparedStatement pstmt = null;
        try {
            m_trx = Trx.get(Trx.createTrxName("merge"), true);
            pstmt = DB.prepareStatement(sql, Trx.createTrxName());
            pstmt.setString(1, ColumnName);
            pstmt.setString(2, ColumnName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String tName = rs.getString(1);
                String cName = rs.getString(2);
                if (!TableName.equals(tName)) {
                    int count = mergeTable(tName, cName, from_ID, to_ID);
                    if (count < 0) success = false; else m_totalCount += count;
                }
            }
            rs.close();
            pstmt.close();
            pstmt = null;
            log.config("Success=" + success + " - " + ColumnName + " - From=" + from_ID + ",To=" + to_ID);
            if (success) {
                sql = "DELETE " + TableName + " WHERE " + ColumnName + "=" + from_ID;
                if (DB.executeUpdate(sql, m_trx.getTrxName()) < 0) {
                    m_errorLog.append(Env.NL).append("DELETE ").append(TableName).append(" - ");
                    success = false;
                    log.config(m_errorLog.toString());
                    m_trx.rollback();
                    return false;
                }
            }
            if (success) m_trx.commit(); else m_trx.rollback();
            m_trx.close();
        } catch (Exception ex) {
            log.log(Level.SEVERE, ColumnName, ex);
        }
        try {
            if (pstmt != null) pstmt.close();
        } catch (Exception ex) {
        }
        pstmt = null;
        return success;
    }
