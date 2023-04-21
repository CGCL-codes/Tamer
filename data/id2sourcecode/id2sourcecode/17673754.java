    @Override
    protected String doIt() throws Exception {
        initialize();
        StringBuffer sql = new StringBuffer("SELECT distinct t.ad_table_id, t.tablename " + "FROM ad_table as t " + "INNER JOIN ad_column as c ON (t.ad_table_id = c.ad_table_id) " + "WHERE t.tablename NOT IN " + elementsToCol(getExcludedTables()));
        if (isJustUID()) {
            sql.append(" AND (c.columnname ilike 'AD_ComponentObjectUID')");
        } else {
            sql.append(" AND (c.columnname ilike 'AD_ComponentVersion_ID')");
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sqlTable;
        StringBuffer uid = new StringBuffer();
        M_Table table;
        List<String> keyColumns;
        try {
            getTrx(get_TrxName()).start();
            ps = DB.prepareStatement(sql.toString(), get_TrxName());
            rs = ps.executeQuery();
            while (rs.next()) {
                table = new M_Table(getCtx(), rs.getInt("ad_table_id"), get_TrxName());
                uid = new StringBuffer("'" + getComponent().getPrefix() + PO.SEPARATORUID + table.getTableName() + "'");
                keyColumns = table.getKeyColumns();
                for (int i = 0; i < keyColumns.size(); i++) {
                    uid.append("||'").append(PO.SEPARATORUID).append("'||").append(keyColumns.get(i));
                }
                sqlTable = getSqlUpdateForTable(table.getTableName(), uid.toString());
                DB.executeUpdate(sqlTable, get_TrxName());
            }
            getTrx(get_TrxName()).commit();
        } catch (Exception e) {
            getTrx(get_TrxName()).rollback();
            log.severe(e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
                if (rs != null) rs.close();
            } catch (Exception e) {
                log.severe(e.getMessage());
                throw new Exception(e.getMessage());
            }
        }
        return "Actualizacion de tablas finalizada correctamente";
    }
