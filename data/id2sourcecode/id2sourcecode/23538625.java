    private boolean saveSettings() {
        DefaultTableModel cModel = (DefaultTableModel) container.getColumnTable().getModel();
        DefaultTableModel cdModel = (DefaultTableModel) container.getColumnDisplayTable().getModel();
        DefaultTableModel cosModel = (DefaultTableModel) container.getColumnOrderSourceTable().getModel();
        DefaultTableModel coModel = (DefaultTableModel) container.getColumnOrderTable().getModel();
        DefaultTableModel caModel = (DefaultTableModel) container.getColumnAllTable().getModel();
        String sql = getSQL();
        int clsId = container.getObject().getClsId();
        int layout = container.getLayout_C();
        String csql = container.getCSQL();
        DAO dao = DAO.getInstance();
        dao.setAutoCommit(false);
        boolean flag = false;
        if (container.getCommand().equals("ADD") || container.getCommand().equals("COPY")) {
            dao.update(Resources.INSERT_CL_SQL);
            dao.setInt(1, clsId);
            dao.setInt(2, layout);
            dao.setString(3, sql);
            dao.setBoolean(4, false);
            dao.setString(5, csql);
            flag = dao.executeUpdate();
        } else if (container.getCommand().equals("EDIT")) {
            dao.update(Resources.UPDATE_CL_SQL);
            dao.setString(1, sql);
            dao.setString(2, csql);
            dao.setInt(3, clsId);
            dao.setInt(4, layout);
            flag = dao.executeUpdate();
        }
        if (flag) {
            dao.update(Resources.DELETE_CLFIELDS_SQL);
            dao.setInt(1, clsId);
            dao.setInt(2, layout);
            dao.executeUpdate();
            for (int i = 0; i < cdModel.getRowCount(); i++) {
                Column column = (Column) cdModel.getValueAt(i, 0);
                dao.update(Resources.INSERT_CLFIELDS_SQL);
                dao.setInt(1, clsId);
                dao.setInt(2, layout);
                dao.setString(3, column.getTableName());
                dao.setString(4, column.getColumnName());
                dao.setInt(5, i + 1);
                dao.setObject(6, cdModel.getValueAt(i, 2));
                dao.setObject(7, cdModel.getValueAt(i, 1));
                int tempRow = findRow(column, container.getColumnAllTable());
                String andorString = (String) caModel.getValueAt(tempRow, 0);
                if (andorString.equals("AND")) dao.setObject(8, true); else dao.setObject(8, false);
                dao.setObject(9, caModel.getValueAt(tempRow, 2));
                tempRow = findRow(column, container.getColumnOrderTable());
                if (tempRow == -1) dao.setObject(10, null); else dao.setInt(10, tempRow + 1);
                if (tempRow == -1) dao.setObject(11, null); else dao.setObject(11, coModel.getValueAt(tempRow, 1));
                flag = dao.executeUpdate();
                if (!flag) {
                    dao.rollback();
                    dao.setAutoCommit(true);
                    return false;
                }
            }
            for (int i = 0; i < caModel.getRowCount(); i++) {
                Column column = (Column) caModel.getValueAt(i, 1);
                String filterString = (String) caModel.getValueAt(i, 2);
                if ((filterString != null) && (filterString.trim().length() > 0)) {
                    dao.query(Resources.SELECT_CLFIELDS_BY_CLSID_AND_LAYOUT_AND_TABLENAME_AND_COLUMNNAME);
                    dao.setObject(1, clsId);
                    dao.setObject(2, layout);
                    dao.setObject(3, column.getTableName());
                    dao.setObject(4, column.getColumnName());
                    ResultSet rs = dao.executeQuery();
                    boolean displayed = false;
                    try {
                        if (rs.next()) {
                            displayed = true;
                        }
                        rs.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!displayed) {
                        dao.update(Resources.INSERT_CLFIELDS_SQL);
                        dao.setInt(1, clsId);
                        dao.setInt(2, layout);
                        dao.setString(3, column.getTableName());
                        dao.setString(4, column.getColumnName());
                        dao.setInt(5, 0);
                        dao.setObject(6, null);
                        dao.setObject(7, null);
                        int tempRow = findRow(column, container.getColumnAllTable());
                        String andorString = (String) caModel.getValueAt(tempRow, 0);
                        if (andorString.equals("AND")) dao.setObject(8, true); else dao.setObject(8, false);
                        dao.setObject(9, caModel.getValueAt(tempRow, 2));
                        dao.setObject(10, null);
                        dao.setObject(11, null);
                        flag = dao.executeUpdate();
                        if (!flag) {
                            dao.rollback();
                            dao.setAutoCommit(true);
                            return false;
                        }
                    }
                }
            }
            if (flag) {
                dao.commit();
                if (container.getCommand().equals("ADD") || container.getCommand().equals("COPY")) {
                    setDefaultName();
                }
                dao.setAutoCommit(true);
                return true;
            } else {
                dao.rollback();
                dao.setAutoCommit(true);
                return false;
            }
        } else {
            dao.rollback();
            dao.setAutoCommit(true);
            return false;
        }
    }
