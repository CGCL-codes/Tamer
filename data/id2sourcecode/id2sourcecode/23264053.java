    public boolean classUpdate_WholeObject(Row row, boolean test) {
        boolean result = true;
        String mainTableName = getMainTableName();
        Vector needModifyColumns = new Vector();
        Vector whereColumns = new Vector();
        StringBuffer sb = new StringBuffer("UPDATE ");
        sb.append(mainTableName + " SET ");
        Iterator columnIterator = getColumns().iterator();
        while (columnIterator.hasNext()) {
            Column column = (Column) columnIterator.next();
            if (column.getTableName().equals(mainTableName)) {
                needModifyColumns.add(column);
                sb.append(column.getColumnName() + "=?,");
            }
        }
        sb.delete(sb.lastIndexOf(","), sb.lastIndexOf(",") + 1);
        sb.append(" WHERE ");
        columnIterator = getColumns().iterator();
        while (columnIterator.hasNext()) {
            Column column = (Column) columnIterator.next();
            if ((column.isPrimaryKey()) && (column.getTableName().equals(mainTableName))) {
                whereColumns.add(column);
                sb.append(column.getColumnName() + "=? AND ");
            }
        }
        sb.delete(sb.lastIndexOf("AND"), sb.lastIndexOf("AND") + 3);
        String sql = sb.toString();
        DAO dao = DAO.getInstance();
        dao.update(sql);
        dao.setAutoCommit(false);
        int currentParameter = 1;
        columnIterator = needModifyColumns.iterator();
        while (columnIterator.hasNext()) {
            Column column = (Column) columnIterator.next();
            Object value = getNewCellValue(column, row);
            dao.setObject(currentParameter, value);
            currentParameter++;
        }
        columnIterator = whereColumns.iterator();
        while (columnIterator.hasNext()) {
            Column column = (Column) columnIterator.next();
            Object value = getOldCellValue(column, row);
            dao.setObject(currentParameter, value);
            currentParameter++;
        }
        result = dao.executeUpdate();
        if (test) {
            dao.rollback();
        } else {
            if (!result) {
                dao.rollback();
            } else {
                dao.commit();
                Iterator rowIter = row.getRowSet().getRows().iterator();
                while (rowIter.hasNext()) {
                    Row childRow = (Row) rowIter.next();
                    if (childRow.isDelete()) classDelete_Child(childRow); else if (childRow.isAdd()) classInsert_Child(childRow, false); else if (childRow.isModify()) classUpdate_Child(childRow, false);
                }
            }
        }
        dao.setAutoCommit(true);
        return result;
    }
