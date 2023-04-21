    public void readMetaData(Connection oConn, String sTable, String sPK) throws SQLException {
        int lenPK;
        int iCurr;
        int cCols;
        Statement oStmt = null;
        ResultSet oRSet = null;
        ResultSetMetaData oMDat = null;
        if (DebugFile.trace) {
            DebugFile.writeln("Begin DataTblDef.readMetaData([Connection], \"" + sTable + "\",\"" + sPK + "\")");
            DebugFile.incIdent();
        }
        BaseTable = sTable;
        try {
            oStmt = oConn.createStatement();
            if (DebugFile.trace) DebugFile.writeln("Statement.executeQuery(SELECT * FROM " + sTable + " WHERE 1=0)");
            oRSet = oStmt.executeQuery("SELECT * FROM " + sTable + " WHERE 1=0");
            oMDat = oRSet.getMetaData();
            cCols = oMDat.getColumnCount();
            alloc(cCols);
            for (int c = 0; c < cCols; c++) {
                ColNames[c] = oMDat.getColumnName(c + 1);
                ColTypes[c] = oMDat.getColumnType(c + 1);
                ColSizes[c] = oMDat.getPrecision(c + 1);
                if (DebugFile.trace) DebugFile.writeln(ColNames[c] + " SQLType " + String.valueOf(ColTypes[c]) + " precision " + ColSizes[c]);
            }
            oMDat = null;
        } catch (SQLException sqle) {
            throw new SQLException(sqle.getMessage(), sqle.getSQLState(), sqle.getErrorCode());
        } finally {
            if (null != oRSet) oRSet.close();
            if (null != oStmt) oStmt.close();
        }
        if (null != sPK) {
            lenPK = sPK.length() - 1;
            cPKs = 1;
            for (int i = 1; i <= lenPK; i++) if (sPK.charAt(i) == ',') cPKs++;
            PrimaryKeys = new String[cPKs];
            iCurr = 0;
            PrimaryKeys[0] = "";
            for (int j = 0; j <= lenPK; j++) if (sPK.charAt(j) != ',') {
                PrimaryKeys[iCurr] += sPK.charAt(j);
            } else {
                if (DebugFile.trace) DebugFile.writeln("PrimaryKeys[" + String.valueOf(iCurr) + "]=" + PrimaryKeys[iCurr]);
                PrimaryKeys[++iCurr] = "";
            }
            if (DebugFile.trace) DebugFile.writeln("PrimaryKeys[" + String.valueOf(iCurr) + "]=" + PrimaryKeys[iCurr]);
            for (int l = 0; l < ColCount; l++) PrimaryKeyMarks[l] = false;
            for (int k = 0; k < cPKs; k++) {
                for (int f = 0; f < ColCount; f++) {
                    PrimaryKeyMarks[f] |= PrimaryKeys[k].equalsIgnoreCase(ColNames[f]);
                }
            }
        } else {
            cPKs = 0;
            PrimaryKeys = null;
        }
        if (DebugFile.trace) {
            DebugFile.decIdent();
            DebugFile.writeln("End DataTblDef.readMetaData()");
        }
    }
