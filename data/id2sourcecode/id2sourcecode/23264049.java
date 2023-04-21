    public boolean classInsert_WholeObject(Row row, boolean test) {
        boolean result = true;
        String mainTableName = getMainTableName();
        StringBuffer sb = new StringBuffer("INSERT INTO ");
        sb.append(mainTableName + "(");
        Iterator columnIter = columns.iterator();
        while (columnIter.hasNext()) {
            Column column = (Column) columnIter.next();
            if (column.getTableName().equals(mainTableName)) sb.append(column.getColumnName() + ",");
        }
        sb.delete(sb.lastIndexOf(","), sb.lastIndexOf(",") + 1);
        sb.append(") VALUES(");
        columnIter = columns.iterator();
        while (columnIter.hasNext()) {
            Column column = (Column) columnIter.next();
            if (column.getTableName().equals(mainTableName)) sb.append(("?,"));
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
            if (column.getTableName().equals(mainTableName)) {
                Object value = getNewCellValue(column, row);
                dao.setObject(currentParameter, value);
                currentParameter++;
            }
        }
        result = dao.executeUpdate();
        if (test) {
            dao.rollback();
        } else {
            if (!result) {
                dao.rollback();
            } else {
                dao.commit();
                if (isSlayerMaster()) {
                    Iterator rowIter = row.getRowSet().getRows().iterator();
                    while (rowIter.hasNext()) {
                        Row childRow = (Row) rowIter.next();
                        if (childRow != row) {
                            if (!childRow.isDelete()) classInsert_Child(childRow, false);
                        }
                    }
                }
            }
        }
        dao.setAutoCommit(true);
        return result;
    }
