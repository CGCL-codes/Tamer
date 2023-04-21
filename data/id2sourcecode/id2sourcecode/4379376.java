    public synchronized void unifyWithDB(ResultSet colDescs) throws SQLException, PoemException {
        Hashtable dbColumns = new Hashtable();
        int dbIndex = 0;
        if (colDescs != null) {
            for (; colDescs.next(); ++dbIndex) {
                String colName = colDescs.getString("COLUMN_NAME");
                Column column = (Column) columnsByName.get(dbms().melatiName(colName));
                if (column == null) {
                    SQLPoemType colType = database.defaultPoemTypeOfColumnMetaData(colDescs);
                    if (troidColumn == null && colName.equals("id") && dbms().canRepresent(colType, TroidPoemType.it) != null) colType = TroidPoemType.it;
                    if (deletedColumn == null && colName.equals("deleted") && dbms().canRepresent(colType, DeletedPoemType.it) != null) colType = DeletedPoemType.it;
                    column = new ExtraColumn(this, dbms().melatiName(colDescs.getString("COLUMN_NAME")), colType, DefinitionSource.sqlMetaData, extrasIndex++);
                    _defineColumn(column);
                    if (info != null) column.createColumnInfo();
                } else {
                    column.assertMatches(colDescs);
                }
                dbColumns.put(column, Boolean.TRUE);
            }
        }
        if (dbIndex == 0) {
            dbCreateTable();
        } else {
            for (int c = 0; c < columns.length; ++c) {
                if (dbColumns.get(columns[c]) == null) dbAddColumn(columns[c]);
            }
        }
        if (troidColumn == null) throw new NoTroidColumnException(this);
        if (info != null) {
            Hashtable dbHasIndexForColumn = new Hashtable();
            ResultSet index = getDatabase().getCommittedConnection().getMetaData().getIndexInfo(null, dbms().getSchema(), dbms().getQuotedName(dbms().unreservedName(getName())), false, true);
            while (index.next()) {
                try {
                    String columnName = dbms().melatiName(index.getString("COLUMN_NAME"));
                    if (columnName != null) {
                        Column column = getColumn(columnName);
                        column.unifyWithIndex(index);
                        dbHasIndexForColumn.put(column, Boolean.TRUE);
                    }
                } catch (NoSuchColumnPoemException e) {
                    throw new UnexpectedExceptionPoemException(e);
                }
            }
            for (int c = 0; c < columns.length; ++c) {
                if (dbHasIndexForColumn.get(columns[c]) != Boolean.TRUE) dbCreateIndex(columns[c]);
            }
        }
        if (PoemThread.inSession()) PoemThread.writeDown();
        String sql = "SELECT " + troidColumn.quotedName() + " FROM " + quotedName() + " ORDER BY " + troidColumn.quotedName() + " DESC";
        try {
            ResultSet maxTroid = getDatabase().getCommittedConnection().createStatement().executeQuery(sql);
            if (database.logSQL()) database.log(new SQLLogEvent(sql));
            if (maxTroid.next()) nextTroid = maxTroid.getInt(1) + 1; else nextTroid = 0;
        } catch (SQLException e) {
            throw new SQLSeriousPoemException(e);
        }
    }
