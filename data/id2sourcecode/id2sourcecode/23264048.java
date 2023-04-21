    public boolean classInsert() {
        boolean flag = true;
        dao = DAO.getInstance();
        dao.setAutoCommit(false);
        dao.query(Resources.SELECT_CLS_TABLES_DESC_SQL);
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
                int columnsCount = tableColumns.size();
                StringBuffer sb = new StringBuffer("INSERT INTO ");
                sb.append(tableName + "(");
                for (int i = 0; i < columnsCount - 1; i++) {
                    Column column = (Column) tableColumns.get(i);
                    sb.append(column.getColumnName() + ",");
                }
                Column column = (Column) tableColumns.get(columnsCount - 1);
                sb.append(column.getColumnName() + ") VALUES(");
                for (int i = 0; i < columnsCount - 1; i++) {
                    sb.append("?,");
                }
                sb.append("?)");
                String sql = sb.toString();
                dao.update(sql);
                int rowsCount = rows.size();
                for (int i = 0; i < rowsCount; i++) {
                    Row row = (Row) rows.get(i);
                    if (row.isAdd()) {
                        for (int j = 1; j <= tableColumns.size(); j++) {
                            column = (Column) tableColumns.get(j - 1);
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
