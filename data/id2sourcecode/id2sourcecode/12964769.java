    public void readColumns(Connection oConn, DatabaseMetaData oMData) throws SQLException {
        int iErrCode;
        Statement oStmt;
        ResultSet oRSet;
        ResultSetMetaData oRData;
        DBColumn oCol;
        String sCol;
        int iCols;
        ListIterator oColIterator;
        String sColName;
        short iSQLType;
        String sTypeName;
        int iPrecision;
        int iDigits;
        int iNullable;
        int iColPos;
        int iDBMS;
        String sGetAllCols = "";
        String sSetPKCols = "";
        String sSetAllCols = "";
        String sSetNoPKCols = "";
        oColumns = new LinkedList();
        oPrimaryKeys = new LinkedList();
        if (DebugFile.trace) {
            DebugFile.writeln("Begin DBTable.readColumns([DatabaseMetaData])");
            DebugFile.incIdent();
            DebugFile.writeln("DatabaseMetaData.getColumns(" + sCatalog + "," + sSchema + "," + sName + ",%)");
        }
        if (oConn.getMetaData().getDatabaseProductName().equals("PostgreSQL")) iDBMS = 2; else if (oConn.getMetaData().getDatabaseProductName().equals("Oracle")) iDBMS = 5; else iDBMS = 0;
        oStmt = oConn.createStatement();
        try {
            if (DebugFile.trace) DebugFile.writeln("Statement.executeQuery(SELECT * FROM " + sName + " WHERE 1=0)");
            oRSet = oStmt.executeQuery("SELECT * FROM " + sName + " WHERE 1=0");
            iErrCode = 0;
        } catch (SQLException sqle) {
            oStmt.close();
            oRSet = null;
            if (DebugFile.trace) DebugFile.writeln("SQLException " + sName + " " + sqle.getMessage());
            iErrCode = sqle.getErrorCode();
            if (iErrCode == 0) iErrCode = -1;
            if (!sqle.getSQLState().equals("42000")) throw new SQLException(sqle.getMessage(), sqle.getSQLState(), sqle.getErrorCode());
        }
        if (0 == iErrCode) {
            if (DebugFile.trace) DebugFile.writeln("ResultSet.getMetaData()");
            oRData = oRSet.getMetaData();
            iCols = oRData.getColumnCount();
            if (DebugFile.trace) DebugFile.writeln("table has " + String.valueOf(iCols) + " columns");
            for (int c = 1; c <= iCols; c++) {
                sColName = oRData.getColumnName(c).toLowerCase();
                sTypeName = oRData.getColumnTypeName(c);
                iSQLType = (short) oRData.getColumnType(c);
                if (iDBMS == 2) switch(iSQLType) {
                    case Types.CHAR:
                    case Types.VARCHAR:
                        iPrecision = oRData.getColumnDisplaySize(c);
                        break;
                    default:
                        iPrecision = oRData.getPrecision(c);
                } else {
                    if (iSQLType == Types.BLOB || iSQLType == Types.CLOB) iPrecision = 2147483647; else iPrecision = oRData.getPrecision(c);
                }
                iDigits = oRData.getScale(c);
                iNullable = oRData.isNullable(c);
                iColPos = c;
                if (5 == iDBMS && iSQLType == Types.NUMERIC && iPrecision <= 6 && iDigits == 0) {
                    oCol = new DBColumn(sName, sColName, (short) Types.SMALLINT, sTypeName, iPrecision, iDigits, iNullable, iColPos);
                } else {
                    oCol = new DBColumn(sName, sColName, iSQLType, sTypeName, iPrecision, iDigits, iNullable, iColPos);
                }
                if (!sColName.equals(DB.dt_created)) oColumns.add(oCol);
            }
            if (DebugFile.trace) DebugFile.writeln("ResultSet.close()");
            oRSet.close();
            oRSet = null;
            oStmt.close();
            oStmt = null;
            if (5 == iDBMS) {
                oStmt = oConn.createStatement();
                if (DebugFile.trace) {
                    if (null == sSchema) DebugFile.writeln("Statement.executeQuery(SELECT NULL AS TABLE_CAT, COLS.OWNER AS TABLE_SCHEM, COLS.TABLE_NAME, COLS.COLUMN_NAME, COLS.POSITION AS KEY_SEQ, COLS.CONSTRAINT_NAME AS PK_NAME FROM USER_CONS_COLUMNS COLS, USER_CONSTRAINTS CONS WHERE CONS.OWNER=COLS.OWNER AND CONS.CONSTRAINT_NAME=COLS.CONSTRAINT_NAME AND CONS.CONSTRAINT_TYPE='P' AND CONS.TABLE_NAME='" + sName.toUpperCase() + "')"); else DebugFile.writeln("Statement.executeQuery(SELECT NULL AS TABLE_CAT, COLS.OWNER AS TABLE_SCHEM, COLS.TABLE_NAME, COLS.COLUMN_NAME, COLS.POSITION AS KEY_SEQ, COLS.CONSTRAINT_NAME AS PK_NAME FROM USER_CONS_COLUMNS COLS, USER_CONSTRAINTS CONS WHERE CONS.OWNER=COLS.OWNER AND CONS.CONSTRAINT_NAME=COLS.CONSTRAINT_NAME AND CONS.CONSTRAINT_TYPE='P' AND CONS.OWNER='" + sSchema.toUpperCase() + "' AND CONS.TABLE_NAME='" + sName.toUpperCase() + "')");
                }
                if (null == sSchema) oRSet = oStmt.executeQuery("SELECT NULL AS TABLE_CAT, COLS.OWNER AS TABLE_SCHEM, COLS.TABLE_NAME, COLS.COLUMN_NAME, COLS.POSITION AS KEY_SEQ, COLS.CONSTRAINT_NAME AS PK_NAME FROM USER_CONS_COLUMNS COLS, USER_CONSTRAINTS CONS WHERE CONS.OWNER=COLS.OWNER AND CONS.CONSTRAINT_NAME=COLS.CONSTRAINT_NAME AND CONS.CONSTRAINT_TYPE='P' AND CONS.TABLE_NAME='" + sName.toUpperCase() + "'"); else oRSet = oStmt.executeQuery("SELECT NULL AS TABLE_CAT, COLS.OWNER AS TABLE_SCHEM, COLS.TABLE_NAME, COLS.COLUMN_NAME, COLS.POSITION AS KEY_SEQ, COLS.CONSTRAINT_NAME AS PK_NAME FROM USER_CONS_COLUMNS COLS, USER_CONSTRAINTS CONS WHERE CONS.OWNER=COLS.OWNER AND CONS.CONSTRAINT_NAME=COLS.CONSTRAINT_NAME AND CONS.CONSTRAINT_TYPE='P' AND CONS.OWNER='" + sSchema.toUpperCase() + "' AND CONS.TABLE_NAME='" + sName.toUpperCase() + "'");
            } else {
                if (DebugFile.trace) DebugFile.writeln("DatabaseMetaData.getPrimaryKeys(" + sCatalog + "," + sSchema + "," + sName + ")");
                oRSet = oMData.getPrimaryKeys(sCatalog, sSchema, sName);
            }
            if (oRSet != null) {
                while (oRSet.next()) {
                    oPrimaryKeys.add(oRSet.getString(4).toLowerCase());
                    sSetPKCols += oRSet.getString(4) + "=? AND ";
                }
                if (DebugFile.trace) DebugFile.writeln("pk cols " + sSetPKCols);
                if (sSetPKCols.length() > 7) sSetPKCols = sSetPKCols.substring(0, sSetPKCols.length() - 5);
                if (DebugFile.trace) DebugFile.writeln("ResultSet.close()");
                oRSet.close();
                oRSet = null;
            }
            if (null != oStmt) {
                oStmt.close();
                oStmt = null;
            }
            oColIterator = oColumns.listIterator();
            while (oColIterator.hasNext()) {
                sCol = ((DBColumn) oColIterator.next()).getName();
                sGetAllCols += sCol + ",";
                sSetAllCols += "?,";
                if (!oPrimaryKeys.contains(sCol) && !sCol.equalsIgnoreCase(DB.dt_created)) sSetNoPKCols += sCol + "=?,";
            }
            if (DebugFile.trace) DebugFile.writeln("get all cols " + sGetAllCols);
            if (sGetAllCols.length() > 0) sGetAllCols = sGetAllCols.substring(0, sGetAllCols.length() - 1); else sGetAllCols = "*";
            if (DebugFile.trace) DebugFile.writeln("set all cols " + sSetAllCols);
            if (sSetAllCols.length() > 0) sSetAllCols = sSetAllCols.substring(0, sSetAllCols.length() - 1);
            if (DebugFile.trace) DebugFile.writeln("set no pk cols " + sSetNoPKCols);
            if (sSetNoPKCols.length() > 0) sSetNoPKCols = sSetNoPKCols.substring(0, sSetNoPKCols.length() - 1);
            if (DebugFile.trace) DebugFile.writeln("set pk cols " + sSetPKCols);
            if (sSetPKCols.length() > 0) {
                sSelect = "SELECT " + sGetAllCols + " FROM " + sName + " WHERE " + sSetPKCols;
                sInsert = "INSERT INTO " + sName + "(" + sGetAllCols + ") VALUES (" + sSetAllCols + ")";
                sUpdate = "UPDATE " + sName + " SET " + sSetNoPKCols + " WHERE " + sSetPKCols;
                sDelete = "DELETE FROM " + sName + " WHERE " + sSetPKCols;
                sExists = "SELECT NULL FROM " + sName + " WHERE " + sSetPKCols;
            } else {
                sSelect = null;
                sInsert = "INSERT INTO " + sName + "(" + sGetAllCols + ") VALUES (" + sSetAllCols + ")";
                sUpdate = null;
                sDelete = null;
                sExists = null;
            }
        }
        if (DebugFile.trace) {
            DebugFile.writeln(sSelect != null ? sSelect : "NO SELECT STATEMENT");
            DebugFile.writeln(sInsert != null ? sInsert : "NO INSERT STATEMENT");
            DebugFile.writeln(sUpdate != null ? sUpdate : "NO UPDATE STATEMENT");
            DebugFile.writeln(sDelete != null ? sDelete : "NO DELETE STATEMENT");
            DebugFile.writeln(sExists != null ? sExists : "NO EXISTS STATEMENT");
            DebugFile.decIdent();
            DebugFile.writeln("End DBTable.readColumns()");
        }
    }
