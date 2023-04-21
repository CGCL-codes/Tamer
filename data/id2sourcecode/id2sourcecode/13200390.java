    public DBSubset getChildsNamed(JDCConnection oConn, String idParent, String sLanguage, int iOrderBy) throws SQLException {
        long lElapsed = 0;
        if (DebugFile.trace) {
            lElapsed = System.currentTimeMillis();
            DebugFile.writeln("Begin Categories.getChildsNamed([Connection], " + (idParent == null ? "null" : idParent) + "," + (sLanguage == null ? "null" : sLanguage) + "," + String.valueOf(iOrderBy) + ")");
            DebugFile.incIdent();
        }
        Object[] aParams = { idParent, sLanguage, idParent, idParent, sLanguage };
        DBSubset oChilds;
        if (iOrderBy > 0) oChilds = new DBSubset(sChildNamedTables, sChildNamedFields, sChildNamedFilter + " UNION SELECT " + "c." + DB.gu_category + ",c." + DB.nm_category + ",c." + DB.nm_category + "," + "c." + DB.nm_icon + ",c." + DB.nm_icon2 + ",c." + DB.gu_owner + " FROM " + DB.k_categories + " c, " + DB.k_cat_tree + " t WHERE c." + DB.gu_category + "=t." + DB.gu_child_cat + " AND " + "t." + DB.gu_parent_cat + "=? AND c." + DB.gu_category + " NOT IN " + "(SELECT " + DB.gu_category + " FROM " + sChildNamedTables + " WHERE " + sChildNamedFilter + ") ORDER BY " + iOrderBy, 32); else oChilds = new DBSubset(sChildNamedTables, sChildNamedFields, sChildNamedFilter + " UNION SELECT " + "c." + DB.gu_category + ",c." + DB.nm_category + ",c." + DB.nm_category + ", " + "c." + DB.nm_icon + ",c." + DB.nm_icon2 + ",c." + DB.gu_owner + " FROM " + DB.k_categories + " c, " + DB.k_cat_tree + " t WHERE c." + DB.gu_category + "=t." + DB.gu_child_cat + " AND " + "t." + DB.gu_parent_cat + "=? AND c." + DB.gu_category + " NOT IN " + "(SELECT " + DB.gu_category + " FROM " + sChildNamedTables + " WHERE " + sChildNamedFilter + ")", 32);
        int iChilds = oChilds.load(oConn, aParams);
        if (DebugFile.trace) {
            DebugFile.writeln(String.valueOf(iChilds) + " childs readed in " + String.valueOf(System.currentTimeMillis() - lElapsed) + " ms");
            DebugFile.decIdent();
            DebugFile.writeln("End Categories.getChildsNamed()");
        }
        return oChilds;
    }
