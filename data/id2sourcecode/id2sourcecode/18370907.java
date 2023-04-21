    private void postData() throws java.sql.SQLException {
        setProgressMaximum(fileMenus.size() + fileRecordTypes.size() + fileFieldTypes.size());
        int progress = 0;
        setProgressValue(0);
        setMessage("Saving to RDB");
        Connection oracleConnection = getDataSource().getConnection();
        try {
            oracleConnection.setAutoCommit(false);
            boolean deleteMenuItems = resultDialog.isRemoveExistingMenus();
            PreparedStatement deleteMenuQuery = null;
            try {
                PreparedStatement insertMenuQuery = null;
                try {
                    Iterator menuIterator = fileMenus.values().iterator();
                    while (menuIterator.hasNext()) {
                        SignalFieldMenu currentMenu = (SignalFieldMenu) menuIterator.next();
                        String currentMenuID = currentMenu.getID();
                        if (deleteMenuItems) {
                            if (deleteMenuQuery == null) {
                                StringBuffer sql = new StringBuffer("DELETE FROM ");
                                sql.append(Main.SCHEMA);
                                sql.append(".SGNL_FLD_MENU WHERE SGNL_FLD_MENU_ID = ?");
                                deleteMenuQuery = oracleConnection.prepareStatement(sql.toString());
                            } else deleteMenuQuery.clearParameters();
                            deleteMenuQuery.setString(1, currentMenuID);
                            deleteMenuQuery.execute();
                        }
                        int menuItemCount = currentMenu.getSize();
                        for (int i = 0; i < menuItemCount; i++) {
                            if (deleteMenuItems || !currentMenu.isMenuItemInDatabase(i)) {
                                if (insertMenuQuery == null) {
                                    StringBuffer sql = new StringBuffer("INSERT INTO ");
                                    sql.append(Main.SCHEMA);
                                    sql.append(".SGNL_FLD_MENU (SGNL_FLD_MENU_ID, FLD_MENU_VAL) VALUES (?, ?)");
                                    insertMenuQuery = oracleConnection.prepareStatement(sql.toString());
                                } else insertMenuQuery.clearParameters();
                                insertMenuQuery.setString(1, currentMenuID);
                                insertMenuQuery.setString(2, currentMenu.getMenuItemAt(i));
                                insertMenuQuery.execute();
                            }
                            if (importCanceled) return;
                        }
                        if (importCanceled) return;
                        setProgressValue(++progress);
                    }
                } finally {
                    if (insertMenuQuery != null) insertMenuQuery.close();
                }
            } finally {
                if (deleteMenuQuery != null) deleteMenuQuery.close();
            }
            PreparedStatement recordTypeInsertQuery = null;
            try {
                PreparedStatement recordTypeUpdateQuery = null;
                try {
                    Iterator recordTypeIterator = fileRecordTypes.values().iterator();
                    while (recordTypeIterator.hasNext()) {
                        EpicsRecordType currentRecordType = (EpicsRecordType) recordTypeIterator.next();
                        if (currentRecordType.isInDatabase()) {
                            if (recordTypeUpdateQuery == null) {
                                StringBuffer sql = new StringBuffer("UPDATE ");
                                sql.append(Main.SCHEMA);
                                sql.append(".SGNL_REC_TYPE SET REC_TYPE_CODE = ?, TYPE_DESC = ? WHERE REC_TYPE_ID = ?");
                                recordTypeUpdateQuery = oracleConnection.prepareStatement(sql.toString());
                            } else recordTypeUpdateQuery.clearParameters();
                            recordTypeUpdateQuery.setString(1, currentRecordType.getCode());
                            recordTypeUpdateQuery.setString(2, currentRecordType.getDescription());
                            recordTypeUpdateQuery.setString(3, currentRecordType.getID());
                            recordTypeUpdateQuery.execute();
                        } else {
                            if (recordTypeInsertQuery == null) {
                                StringBuffer sql = new StringBuffer("INSERT INTO ");
                                sql.append(Main.SCHEMA);
                                sql.append(".SGNL_REC_TYPE (REC_TYPE_ID, REC_TYPE_CODE, TYPE_DESC) VALUES (?, ?, ?)");
                                recordTypeInsertQuery = oracleConnection.prepareStatement(sql.toString());
                            } else recordTypeInsertQuery.clearParameters();
                            recordTypeInsertQuery.setString(1, currentRecordType.getID());
                            recordTypeInsertQuery.setString(2, currentRecordType.getCode());
                            recordTypeInsertQuery.setString(3, currentRecordType.getDescription());
                            recordTypeInsertQuery.execute();
                        }
                        if (importCanceled) return;
                        setProgressValue(++progress);
                    }
                } finally {
                    if (recordTypeUpdateQuery != null) recordTypeUpdateQuery.close();
                }
            } finally {
                if (recordTypeInsertQuery != null) recordTypeInsertQuery.close();
            }
            PreparedStatement fieldTypeInsertQuery = null;
            try {
                PreparedStatement fieldTypeUpdateQuery = null;
                try {
                    PreparedStatement deviceFieldTypeUpdateQuery = null;
                    try {
                        Iterator fieldTypeIterator = fileFieldTypes.iterator();
                        while (fieldTypeIterator.hasNext()) {
                            SignalFieldType currentFieldType = (SignalFieldType) fieldTypeIterator.next();
                            if (!invalidFieldTypes.contains(currentFieldType.getRecordType().getID())) if (currentFieldType.isInDatabase()) {
                                String currentFieldID = currentFieldType.getID();
                                EpicsRecordType currentRecordType = currentFieldType.getRecordType();
                                if (findFieldTypeIndex(deviceLineFieldTypes, currentFieldID, currentRecordType) >= 0) {
                                    if (deviceFieldTypeUpdateQuery == null) {
                                        StringBuffer sql = new StringBuffer("UPDATE ");
                                        sql.append(Main.SCHEMA);
                                        sql.append(".SGNL_FLD_DEF SET SGNL_FLD_MENU_ID = ? WHERE REC_TYPE_ID = ? AND FLD_ID = ?");
                                        fieldTypeUpdateQuery = oracleConnection.prepareStatement(sql.toString());
                                    } else deviceFieldTypeUpdateQuery.clearParameters();
                                    fieldTypeUpdateQuery.setString(1, currentFieldType.getMenu().getID());
                                    fieldTypeUpdateQuery.setString(2, currentRecordType.getID());
                                    fieldTypeUpdateQuery.setString(3, currentFieldID);
                                    fieldTypeUpdateQuery.execute();
                                } else {
                                    if (fieldTypeUpdateQuery == null) {
                                        StringBuffer sql = new StringBuffer("UPDATE ");
                                        sql.append(Main.SCHEMA);
                                        sql.append(".SGNL_FLD_DEF SET FLD_TYPE_ID = ?, PRMPT_ORD = ?, FLD_DESC = ?, FLD_INIT = ?, FLD_PRMT_GRP = ?, SGNL_FLD_MENU_ID = ? WHERE REC_TYPE_ID = ? AND FLD_ID = ?");
                                        fieldTypeUpdateQuery = oracleConnection.prepareStatement(sql.toString());
                                    } else fieldTypeUpdateQuery.clearParameters();
                                    fieldTypeUpdateQuery.setString(1, currentFieldType.getEpicsFieldTypeID());
                                    fieldTypeUpdateQuery.setInt(2, currentFieldType.getPromptOrder());
                                    fieldTypeUpdateQuery.setString(3, currentFieldType.getDescription());
                                    fieldTypeUpdateQuery.setString(4, currentFieldType.getInitial());
                                    fieldTypeUpdateQuery.setString(5, currentFieldType.getPromptGroup());
                                    SignalFieldMenu currentMenu = currentFieldType.getMenu();
                                    if (currentMenu == null) fieldTypeUpdateQuery.setString(6, null); else fieldTypeUpdateQuery.setString(6, currentMenu.getID());
                                    fieldTypeUpdateQuery.setString(7, currentRecordType.getID());
                                    fieldTypeUpdateQuery.setString(8, currentFieldID);
                                    fieldTypeUpdateQuery.executeUpdate();
                                }
                            } else {
                                if (fieldTypeInsertQuery == null) {
                                    StringBuffer sql = new StringBuffer("INSERT INTO ");
                                    sql.append(Main.SCHEMA);
                                    sql.append(".SGNL_FLD_DEF (REC_TYPE_ID, FLD_ID, FLD_TYPE_ID, PRMPT_ORD, FLD_DESC, FLD_INIT, FLD_PRMT_GRP, SGNL_FLD_MENU_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                                    fieldTypeInsertQuery = oracleConnection.prepareStatement(sql.toString());
                                } else fieldTypeInsertQuery.clearParameters();
                                fieldTypeInsertQuery.setString(1, currentFieldType.getRecordType().getID());
                                fieldTypeInsertQuery.setString(2, currentFieldType.getID());
                                fieldTypeInsertQuery.setString(3, currentFieldType.getEpicsFieldTypeID());
                                fieldTypeInsertQuery.setInt(4, currentFieldType.getPromptOrder());
                                fieldTypeInsertQuery.setString(5, currentFieldType.getDescription());
                                fieldTypeInsertQuery.setString(6, currentFieldType.getInitial());
                                fieldTypeInsertQuery.setString(7, currentFieldType.getPromptGroup());
                                SignalFieldMenu currentMenu = currentFieldType.getMenu();
                                if (currentMenu == null) fieldTypeInsertQuery.setString(8, null); else fieldTypeInsertQuery.setString(8, currentMenu.getID());
                                fieldTypeInsertQuery.execute();
                            }
                            if (importCanceled) return;
                            setProgressValue(++progress);
                        }
                    } finally {
                        if (deviceFieldTypeUpdateQuery != null) deviceFieldTypeUpdateQuery.close();
                    }
                } finally {
                    if (fieldTypeUpdateQuery != null) fieldTypeUpdateQuery.close();
                }
            } finally {
                if (fieldTypeInsertQuery != null) fieldTypeInsertQuery.close();
            }
            if (importCanceled) oracleConnection.rollback(); else oracleConnection.commit();
        } catch (java.lang.RuntimeException ex) {
            oracleConnection.rollback();
            throw ex;
        } catch (java.sql.SQLException ex) {
            oracleConnection.rollback();
            throw ex;
        } finally {
            oracleConnection.close();
            setProgressValue(0);
        }
    }
