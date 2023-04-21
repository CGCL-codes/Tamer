    public boolean load(JDCConnection oConn, Object aPK[]) throws SQLException {
        ResultSet oRSet;
        PreparedStatement oStmt;
        CallableStatement oCall;
        String sNmPageSet;
        Object sField;
        boolean bRetVal;
        if (DebugFile.trace) {
            DebugFile.writeln("Begin PageSetDB.load([Connection], {" + aPK[0] + "}");
            DebugFile.incIdent();
        }
        if (oConn.getDataBaseProduct() == JDCConnection.DBMS_ORACLE || oConn.getDataBaseProduct() == JDCConnection.DBMS_MSSQL) {
            if (DebugFile.trace) DebugFile.writeln("Connection.prepareCall({ call k_sp_read_pageset ('" + aPK[0] + "',?,?,?,?,?,?,?,?,?,?,?,?,?) }");
            oCall = oConn.prepareCall("{ call k_sp_read_pageset (?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
            clear();
            oCall.setObject(1, aPK[0], Types.CHAR);
            oCall.registerOutParameter(2, Types.CHAR);
            oCall.registerOutParameter(3, Types.VARCHAR);
            oCall.registerOutParameter(4, Types.CHAR);
            oCall.registerOutParameter(5, Types.VARCHAR);
            oCall.registerOutParameter(6, Types.VARCHAR);
            oCall.registerOutParameter(7, Types.CHAR);
            oCall.registerOutParameter(8, Types.TIMESTAMP);
            oCall.registerOutParameter(9, Types.VARCHAR);
            oCall.registerOutParameter(10, Types.VARCHAR);
            oCall.registerOutParameter(11, Types.VARCHAR);
            oCall.registerOutParameter(12, Types.VARCHAR);
            oCall.registerOutParameter(13, Types.CHAR);
            oCall.registerOutParameter(14, Types.CHAR);
            if (DebugFile.trace) DebugFile.writeln("CallableStatement.execute()");
            oCall.execute();
            sNmPageSet = oCall.getString(5);
            bRetVal = (null != sNmPageSet);
            put(DB.gu_pageset, aPK[0]);
            if (bRetVal) {
                put(DB.gu_microsite, oCall.getString(2).trim());
                put(DB.nm_microsite, oCall.getString(3));
                put(DB.gu_workarea, oCall.getString(4).trim());
                put(DB.nm_pageset, oCall.getString(5));
                sField = oCall.getObject(6);
                if (!oCall.wasNull()) put(DB.vs_stamp, (String) sField);
                sField = oCall.getObject(7);
                if (!oCall.wasNull()) put(DB.id_language, ((String) sField).trim());
                sField = oCall.getObject(8);
                if (!oCall.wasNull()) put(DB.dt_modified, oCall.getDate(8));
                sField = oCall.getObject(9);
                if (!oCall.wasNull()) put(DB.path_data, (String) sField);
                sField = oCall.getObject(10);
                if (!oCall.wasNull()) put(DB.id_status, (String) sField);
                sField = oCall.getObject(11);
                if (!oCall.wasNull()) put(DB.path_metadata, (String) sField);
                sField = oCall.getObject(12);
                if (!oCall.wasNull()) put(DB.tx_comments, (String) sField);
                sField = oCall.getObject(13);
                if (!oCall.wasNull()) put(DB.gu_company, sField.toString().trim());
                sField = oCall.getObject(14);
                if (!oCall.wasNull()) put(DB.gu_project, sField.toString().trim());
            }
            oCall.close();
        } else {
            if (DebugFile.trace) DebugFile.writeln("Connection.prepareStatement(SELECT m." + DB.gu_microsite + ",m." + DB.nm_microsite + ",p." + DB.gu_workarea + ",p." + DB.nm_pageset + ",p." + DB.vs_stamp + ",p." + DB.id_language + ",p." + DB.dt_modified + ",p." + DB.path_data + ",p." + DB.id_status + ",m." + DB.path_metadata + ",p." + DB.tx_comments + ",p." + DB.gu_company + ",p." + DB.gu_project + " FROM " + DB.k_pagesets + " p LEFT OUTER JOIN " + DB.k_microsites + " m ON p." + DB.gu_microsite + "=m." + DB.gu_microsite + " WHERE p." + DB.gu_pageset + "=" + aPK[0] + ")");
            oStmt = oConn.prepareStatement("SELECT m." + DB.gu_microsite + ",m." + DB.nm_microsite + ",p." + DB.gu_workarea + ",p." + DB.nm_pageset + ",p." + DB.vs_stamp + ",p." + DB.id_language + ",p." + DB.dt_modified + ",p." + DB.path_data + ",p." + DB.id_status + ",m." + DB.path_metadata + ",p." + DB.tx_comments + ",p." + DB.gu_company + ",p." + DB.gu_project + " FROM " + DB.k_pagesets + " p LEFT OUTER JOIN " + DB.k_microsites + " m ON p." + DB.gu_microsite + "=m." + DB.gu_microsite + " WHERE p." + DB.gu_pageset + "=?", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            oStmt.setObject(1, aPK[0], Types.CHAR);
            oRSet = oStmt.executeQuery();
            bRetVal = oRSet.next();
            put(DB.gu_pageset, aPK[0]);
            if (bRetVal) {
                put(DB.gu_microsite, oRSet.getString(1));
                put(DB.nm_microsite, oRSet.getString(2));
                put(DB.gu_workarea, oRSet.getString(3));
                put(DB.nm_pageset, oRSet.getString(4));
                sField = oRSet.getObject(5);
                if (!oRSet.wasNull()) put(DB.vs_stamp, (String) sField);
                sField = oRSet.getObject(6);
                if (!oRSet.wasNull()) put(DB.id_language, (String) sField);
                sField = oRSet.getObject(7);
                if (!oRSet.wasNull()) put(DB.dt_modified, oRSet.getTimestamp(7));
                sField = oRSet.getObject(8);
                if (!oRSet.wasNull()) put(DB.path_data, (String) sField);
                sField = oRSet.getObject(9);
                if (!oRSet.wasNull()) put(DB.id_status, (String) sField);
                sField = oRSet.getObject(10);
                if (!oRSet.wasNull()) put(DB.path_metadata, (String) sField);
                sField = oRSet.getObject(11);
                if (!oRSet.wasNull()) put(DB.tx_comments, (String) sField);
                sField = oRSet.getObject(12);
                if (!oRSet.wasNull()) put(DB.gu_company, (String) sField);
                sField = oRSet.getObject(13);
                if (!oRSet.wasNull()) put(DB.gu_project, (String) sField);
            }
            oRSet.close();
            oStmt.close();
        }
        if (DebugFile.trace) {
            DebugFile.decIdent();
            DebugFile.writeln("End PageSetDB.load() : " + String.valueOf(bRetVal));
        }
        return bRetVal;
    }
