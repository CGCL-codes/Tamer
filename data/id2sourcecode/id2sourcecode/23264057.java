    public boolean classUpdate() {
        boolean flag = true;
        dao = DAO.getInstance();
        dao.setAutoCommit(false);
        dao.query(Resources.SELECT_CLS_TABLES_SQL);
        dao.setInt(1, clsId);
        ResultSet rs = dao.executeQuery();
        try {
            while (rs.next()) {
                String mainTableName = rs.getString("PARENTTABLE");
                boolean isMainTable = (mainTableName == null);
                String tableName = rs.getString("TABLENAME");
                if (mainTableName == null) {
                    mainTableName = tableName;
                }
                Vector tableColumns = new Vector();
                Iterator columnsIter = columns.iterator();
                while (columnsIter.hasNext()) {
                    Column column = (Column) columnsIter.next();
                    if (column.getTableName().equals(tableName)) {
                        tableColumns.add(column);
                    }
                }
                Vector needModifyColumns = new Vector();
                Vector whereColumns = new Vector();
                int columnsCount = tableColumns.size();
                StringBuffer sb = new StringBuffer("UPDATE ");
                sb.append(tableName + " SET ");
                for (int i = 0; i < columnsCount - 1; i++) {
                    Column column = (Column) tableColumns.get(i);
                    needModifyColumns.add(column);
                    sb.append(column.getColumnName() + "=?,");
                }
                Column column = (Column) tableColumns.get(columnsCount - 1);
                needModifyColumns.add(column);
                sb.append(column.getColumnName() + "=? WHERE ");
                for (int i = 0; i < columnsCount; i++) {
                    column = (Column) tableColumns.get(i);
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
                    if (row.isModify()) {
                        if (isMainTable) {
                            for (int j = 1; j <= needModifyColumns.size(); j++) {
                                column = (Column) needModifyColumns.get(j - 1);
                                Vector newCells = row.getNewCells();
                                int cellsCount = newCells.size();
                                for (int k = 0; k <= cellsCount; k++) {
                                    Cell cell = (Cell) newCells.get(k);
                                    if ((cell.getColumnName().equals(column.getColumnName())) && (cell.getTableName().equals(column.getTableName()))) {
                                        dao.setObject(j, cell.getColumnValue());
                                        break;
                                    }
                                }
                            }
                        } else {
                            for (int j = 1; j <= needModifyColumns.size(); j++) {
                                column = (Column) needModifyColumns.get(j - 1);
                                Vector newCells = row.getNewCells();
                                int cellsCount = newCells.size();
                                for (int k = 0; k <= cellsCount; k++) {
                                    Cell cell = (Cell) newCells.get(k);
                                    if ((cell.getColumnName().equals(column.getColumnName())) && (cell.getTableName().equals(column.getTableName()))) {
                                        if (!cell.getTableName().equals(mainTableName)) {
                                            Iterator newCellIter = newCells.iterator();
                                            while (newCellIter.hasNext()) {
                                                Cell tempCell = (Cell) newCellIter.next();
                                                if ((tempCell.getColumnName().equals(cell.getColumnName())) && (tempCell.getTableName().equals(mainTableName))) {
                                                    cell.setColumnValue(tempCell.getColumnValue());
                                                }
                                            }
                                        }
                                        dao.setObject(j, cell.getColumnValue());
                                        break;
                                    }
                                }
                            }
                        }
                        for (int j = 1; j <= whereColumns.size(); j++) {
                            column = (Column) whereColumns.get(j - 1);
                            Vector oldCells = row.getOldCells();
                            int cellsCount = oldCells.size();
                            for (int k = 0; k <= cellsCount; k++) {
                                Cell cell = (Cell) oldCells.get(k);
                                if (cell.getColumnName().equals(column.getColumnName())) {
                                    dao.setObject(j + needModifyColumns.size(), cell.getColumnValue());
                                    break;
                                }
                            }
                        }
                        if (!dao.executeUpdate()) {
                            flag = false;
                            break;
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
