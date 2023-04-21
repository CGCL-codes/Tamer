    public boolean classInsert_Child(Row row, boolean test) {
        boolean result = true;
        String tableName = getChildTableName();
        StringBuffer sb = new StringBuffer("INSERT INTO ");
        sb.append(tableName + "(");
        Iterator columnIter = columns.iterator();
        while (columnIter.hasNext()) {
            Column column = (Column) columnIter.next();
            if (column.getTableName().equals(tableName)) sb.append(column.getColumnName() + ",");
        }
        sb.delete(sb.lastIndexOf(","), sb.lastIndexOf(",") + 1);
        sb.append(") VALUES(");
        columnIter = columns.iterator();
        while (columnIter.hasNext()) {
            Column column = (Column) columnIter.next();
            if (column.getTableName().equals(tableName)) sb.append(("?,"));
        }
        sb.delete(sb.lastIndexOf(","), sb.lastIndexOf(",") + 1);
        sb.append(")");
        String sql = sb.toString();
        DAO dao = DAO.getInstance();
        dao.update(sql);
        dao.setAutoCommit(false);
        int currentParameter = 1;
        columnIter = columns.iterator();
        while (columnIter.hasNext()) {
            Column column = (Column) columnIter.next();
            if (column.getTableName().equals(tableName)) {
                Object value = getNewCellValue(column, row);
                dao.setObject(currentParameter, value);
                currentParameter++;
            }
        }
        result = dao.executeUpdate();
        if (test) dao.rollback(); else {
            if (!result) dao.rollback(); else dao.commit();
        }
        dao.setAutoCommit(true);
        return result;
    }
