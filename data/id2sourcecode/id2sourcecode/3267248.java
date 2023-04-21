    protected void getRows(Object[] OrPK, Object[] TrPK, int cParams, int iTable) throws SQLException {
        int iPK;
        int iFetchBurst = 500;
        int iSQLType;
        int cTransforms;
        Vector oRow;
        HashMap oTransforms;
        Object oOriginalValue;
        Object oTransformedValue;
        ResultSet oRSet;
        ResultSetMetaData oRDat;
        DataRowSet oDatR;
        String sColName;
        DataTblDef oMDat = OrMetaData[iTable];
        DataTransformation oDatT;
        PreparedStatement oStmt = OrStatements[iTable];
        PreparedStatement oStmt2;
        oDatR = getRowSet(iTable);
        if (oDatR.WhereClause != null) {
            if (oDatR.WhereClause.indexOf("?") > 0) {
                for (int p = 0; p < cParams; p++) {
                    if (DebugFile.trace) DebugFile.writeln("binding query input parameter " + String.valueOf(p + 1));
                    oStmt.setObject(p + 1, OrPK[p]);
                }
            }
        }
        if (DebugFile.trace) DebugFile.writeln("PreparedStatement.executeQuery()");
        oRSet = oStmt.executeQuery();
        oDatR = null;
        if (DebugFile.trace) {
            if (DataRowSets.get(iTable) != null) DebugFile.writeln("FieldList=" + getRowSet(iTable).FieldList); else DebugFile.writeln("ERROR: getRowSet(" + String.valueOf(iTable) + ") == null");
        }
        if (getRowSet(iTable).FieldList.compareTo("*") != 0) iCols = oRSet.getMetaData().getColumnCount(); else iCols = TrMetaData[iTable].ColCount;
        if (DebugFile.trace) DebugFile.writeln("reading " + String.valueOf(iCols) + " columns");
        oResults = new Vector(iFetchBurst, iFetchBurst);
        if (DebugFile.trace) DebugFile.writeln("new Vector(" + String.valueOf(iFetchBurst) + ")");
        iRows = 0;
        try {
            oTransforms = (HashMap) Transformations.get(iTable);
            cTransforms = oTransforms.size();
        } catch (ArrayIndexOutOfBoundsException e) {
            if (DebugFile.trace) DebugFile.writeln("table has no transformation replacements");
            oTransforms = null;
            cTransforms = 0;
        }
        if (0 == cTransforms) {
            while (oRSet.next() && iRows < iFetchBurst) {
                iRows++;
                if (DebugFile.trace) DebugFile.writeln("caching row " + String.valueOf(iRows));
                oRow = new Vector(iCols);
                for (int c = 1; c <= iCols; c++) oRow.add(oRSet.getObject(c));
                oResults.add(oRow);
            }
        } else {
            oRDat = oRSet.getMetaData();
            while (oRSet.next() && iRows < iFetchBurst) {
                iRows++;
                if (DebugFile.trace) DebugFile.writeln("caching row " + String.valueOf(iRows));
                oRow = new Vector(iCols);
                iPK = 0;
                for (int c = 1; c <= iCols; c++) {
                    try {
                        sColName = oRDat.getColumnName(c);
                        oDatT = (DataTransformation) oTransforms.get(sColName);
                        if (null == oDatT) oRow.add(oRSet.getObject(c)); else {
                            oOriginalValue = oRSet.getObject(c);
                            oTransformedValue = oDatT.transform(getOriginConnection(), getTargetConnection(), oOriginalValue);
                            if (DebugFile.trace) DebugFile.writeln(sColName + " " + oOriginalValue + " transformed to " + (oTransformedValue != null ? oTransformedValue : "NULL"));
                            oRow.add(oTransformedValue);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        oRow.add(oRSet.getObject(c));
                    }
                }
                oResults.add(oRow);
            }
            oRDat = null;
        }
        if (DebugFile.trace) DebugFile.writeln("row count = " + String.valueOf(iRows));
        oRSet.close();
        oRSet = null;
    }
