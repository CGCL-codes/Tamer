    public boolean classDelete() {
        boolean flag = true;
        dao = DAO.getInstance();
        dao.setAutoCommit(false);
        dao.query(Resources.SELECT_CLS_TABLES_DESC_SQL);
        dao.setInt(1, clsId);
        ResultSet rs = dao.executeQuery();
        try {
            while (rs.next()) {
                String tableName = rs.getString("TABLENAME");
                Vector tableColumns = new Vector();
                Iterator columnsIter = columns.iterator();
                while (columnsIter.hasNext()) {
                    Column column = (Column) columnsIter.next();
                    if (column.getTableName().equals(tableName)) {
                        tableColumns.add(column);
                    }
                }
                Vector whereColumns = new Vector();
                int columnsCount = tableColumns.size();
                StringBuffer sb = new StringBuffer("DELETE FROM ");
                sb.append(tableName + " WHERE ");
                for (int i = 0; i < columnsCount; i++) {
                    Column column = (Column) tableColumns.get(i);
                    if (column.isPrimaryKey()) {
                        whereColumns.add(column);
                        sb.append(column.getColumnName() + "=? AND ");
                    }
                }
                sb.delete(sb.lastIndexOf("AND"), sb.lastIndexOf("AND") + 3);
                String sql = sb.toString();
                dao.update(sql);
                int rowsCount = rows.size();
                for (int i = 0; i < rowsCount; i++) {
                    Row row = (Row) rows.get(i);
                    if (row.isDelete()) {
                        for (int j = 1; j <= whereColumns.size(); j++) {
                            Column column = (Column) whereColumns.get(j - 1);
                            Vector oldCells = row.getOldCells();
                            int cellsCount = oldCells.size();
                            for (int k = 0; k <= cellsCount; k++) {
                                Cell cell = (Cell) oldCells.get(k);
                                if ((cell.getColumnName().equals(column.getColumnName())) && (cell.getTableName().equals(column.getTableName()))) {
                                    dao.setObject(j, cell.getColumnValue());
                                    break;
                                }
                            }
                        }
                        if (!dao.executeUpdate()) {
                            flag = false;
                            break;
                        } else {
                            this.rows.remove(row);
                        }
                    }
                }
                if (flag) {
                    dao.commit();
                } else {
                    dao.rollback();
                    break;
                }
            }
            rs.close();
            dao.setAutoCommit(true);
        } catch (SQLException sqle) {
        }
        return flag;
    }
