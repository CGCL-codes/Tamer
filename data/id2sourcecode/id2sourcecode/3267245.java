    public void prepareStatements() throws SQLException {
        HashMap oMap;
        String sSQL;
        boolean bIsMapped;
        boolean bHasDefault;
        int iTrCols;
        String sCol;
        int iCol;
        int c;
        if (DebugFile.trace) {
            DebugFile.writeln("Begin DataStruct.prepareStatements()");
            DebugFile.incIdent();
        }
        oInsertTexts = new HashMap(2 * cTables);
        oSelectTexts = new HashMap(2 * cTables);
        OrStatements = new PreparedStatement[cTables];
        TrStatements = new PreparedStatement[cTables];
        UpStatements = new PreparedStatement[cTables];
        DlStatements = new PreparedStatement[cTables];
        OrMetaData = new DataTblDef[cTables];
        TrMetaData = new DataTblDef[cTables];
        for (int s = 0; s < cTables; s++) {
            if (CONNECTED == iTrStatus || REFERENCED == iTrStatus) {
                TrMetaData[s] = new DataTblDef();
                if (DebugFile.trace) DebugFile.writeln("DataTblDef.readMetaData (TargetConnection, " + getRowSet(s).TargetTable + ", " + (String) oToPKs.get(getRowSet(s).TargetTable) + ")");
                if (oToPKs.get(getRowSet(s).TargetTable) != null) TrMetaData[s].readMetaData(oTrConn, getRowSet(s).TargetTable, oToPKs.get(getRowSet(s).TargetTable).toString()); else TrMetaData[s].readMetaData(oTrConn, getRowSet(s).TargetTable, null);
                sSQL = "DELETE FROM " + getRowSet(s).TargetTable;
                if (!isEmpty(getRowSet(s).EraseClause)) sSQL += " WHERE " + getRowSet(s).EraseClause; else if (!isEmpty(getRowSet(s).WhereClause)) sSQL += " WHERE " + getRowSet(s).WhereClause;
                if (DebugFile.trace) DebugFile.writeln("Connection.prepareStatement(" + sSQL + ")");
                DlStatements[s] = oTrConn.prepareStatement(sSQL);
                iTrCols = TrMetaData[s].ColCount;
                sSQL = "INSERT INTO " + getRowSet(s).TargetTable + " VALUES (";
                for (c = iTrCols; c >= 1; c--) sSQL += (c != 1) ? "?," : "?)";
                if (DebugFile.trace) DebugFile.writeln("Connection.prepareStatement(" + sSQL + ")");
                oInsertTexts.put(getRowSet(s).OriginTable, sSQL);
                TrStatements[s] = oTrConn.prepareStatement(sSQL);
                sSQL = "UPDATE " + getRowSet(s).TargetTable + " SET ";
                for (c = 0; c < iTrCols; c++) if (!TrMetaData[s].isPrimaryKey(c)) sSQL += TrMetaData[s].ColNames[c] + "=?,";
                sSQL = sSQL.substring(0, sSQL.length() - 1) + " WHERE ";
                for (c = 0; c < TrMetaData[s].cPKs; c++) sSQL += TrMetaData[s].PrimaryKeys[c] + "=? AND ";
                sSQL = sSQL.substring(0, sSQL.length() - 5);
                if (DebugFile.trace) DebugFile.writeln("Connection.prepareStatement(" + sSQL + ")");
                UpStatements[s] = oTrConn.prepareStatement(sSQL);
            }
            if (CONNECTED == iOrStatus || REFERENCED == iOrStatus) {
                OrMetaData[s] = new DataTblDef();
                if (DebugFile.trace) DebugFile.writeln("DataTblDef.readMetaData (OriginConnection, " + getRowSet(s).OriginTable + ", " + (String) oToPKs.get(getRowSet(s).OriginTable) + ")");
                OrMetaData[s].readMetaData(oOrConn, getRowSet(s).OriginTable, (String) oFromPKs.get(getRowSet(s).OriginTable));
                if (CONNECTED == iTrStatus || REFERENCED == iTrStatus) iTrCols = TrMetaData[s].ColCount; else iTrCols = OrMetaData[s].ColCount;
                if (DebugFile.trace) DebugFile.writeln("Column count = " + String.valueOf(iTrCols));
                if (getRowSet(s).FieldList.compareTo("*") != 0) {
                    sSQL = "SELECT " + getRowSet(s).FieldList + " ";
                } else {
                    sSQL = "SELECT ";
                    for (c = 0; c < iTrCols; c++) {
                        sCol = TrMetaData[s].ColNames[c];
                        try {
                            oMap = (HashMap) FieldMaps.get(s);
                            bIsMapped = oMap.containsKey(sCol);
                            if (bIsMapped) sCol = (String) oMap.get(sCol); else {
                                bIsMapped = oMap.containsKey(sCol.toUpperCase());
                                if (bIsMapped) sCol = (String) oMap.get(sCol.toUpperCase()); else {
                                    bIsMapped = oMap.containsKey(sCol.toLowerCase());
                                    if (bIsMapped) sCol = (String) oMap.get(sCol.toLowerCase());
                                }
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            bIsMapped = false;
                        }
                        iCol = OrMetaData[s].findColumnPosition(sCol);
                        if (iCol != -1) sSQL += sCol + ((c < iTrCols - 1) ? "," : " "); else {
                            try {
                                oMap = (HashMap) FieldDefs.get(s);
                                bHasDefault = oMap.containsKey(sCol);
                            } catch (ArrayIndexOutOfBoundsException e) {
                                bHasDefault = false;
                                oMap = null;
                            }
                            if (bHasDefault) sSQL += (String) oMap.get(sCol) + " AS " + TrMetaData[s].ColNames[c] + ((c < iTrCols - 1) ? "," : " "); else if (bIsMapped) sSQL += sCol + " AS " + TrMetaData[s].ColNames[c] + ((c < iTrCols - 1) ? "," : " "); else sSQL += "NULL AS " + TrMetaData[s].ColNames[c] + ((c < iTrCols - 1) ? "," : " ");
                        }
                    }
                }
                sSQL += "FROM " + getRowSet(s).OriginTable;
                if (!isEmpty(getRowSet(s).WhereClause)) {
                    if (getRowSet(s).WhereClause.trim().toUpperCase().startsWith("START")) sSQL += " " + getRowSet(s).WhereClause; else sSQL += " WHERE " + getRowSet(s).WhereClause;
                }
                if (DebugFile.trace) DebugFile.writeln("Connection.prepareStatement(" + sSQL + ")");
                oSelectTexts.put(getRowSet(s).OriginTable, sSQL);
                OrStatements[s] = oOrConn.prepareStatement(sSQL, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            }
        }
        if (CONNECTED == iOrStatus || REFERENCED == iOrStatus) bOrPrepared = true;
        if (CONNECTED == iTrStatus || REFERENCED == iTrStatus) bTrPrepared = true;
        if (DebugFile.trace) {
            DebugFile.decIdent();
            DebugFile.writeln("End DataStruct.prepareStatements()");
        }
    }
