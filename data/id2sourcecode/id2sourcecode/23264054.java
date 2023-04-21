    public boolean classUpdate_Child(Row row, boolean test) {
        boolean result = true;
        String childTableName = getChildTableName();
        Vector needModifyColumns = new Vector();
        Vector whereColumns = new Vector();
        StringBuffer sb = new StringBuffer("UPDATE ");
        sb.append(childTableName + " SET ");
        Iterator columnIterator = getColumns().iterator();
        while (columnIterator.hasNext()) {
            Column column = (Column) columnIterator.next();
            if (column.getTableName().equals(childTableName)) {
                needModifyColumns.add(column);
                sb.append(column.getColumnName() + "=?,");
            }
        }
        sb.delete(sb.lastIndexOf(","), sb.lastIndexOf(",") + 1);
        sb.append(" WHERE ");
        columnIterator = getColumns().iterator();
        while (columnIterator.hasNext()) {
            Column column = (Column) columnIterator.next();
            if ((column.isPrimaryKey()) && (column.getTableName().equals(childTableName))) {
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
        if (test) dao.rollback(); else {
            if (!result) dao.rollback(); else dao.commit();
        }
        dao.setAutoCommit(true);
        return result;
    }
