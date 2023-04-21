    @Override
    public Vector getAttSpectrumDataLast_n(final String attributeName, final int number, final int writable) throws ArchivingException {
        final IDBConnection dbConn = ConnectionFactory.getInstance(archType);
        final IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(archType);
        final IDbUtils dbUtils = DbUtilsFactory.getInstance(archType);
        if (dbConn == null || att == null || dbUtils == null) {
            return null;
        }
        final Vector tmpSpectrumVect = new Vector();
        final int data_type = att.getAtt_TFW_Data(attributeName)[0];
        final boolean roFields = writable == AttrWriteType._READ || writable == AttrWriteType._WRITE;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(attributeName);
        String selectField0;
        String selectField1;
        String selectField2;
        String selectField3 = null;
        String selectFields;
        String orderField;
        if (roFields) {
            selectField0 = ConfigConst.TAB_SPECTRUM_RO[0];
            selectField1 = ConfigConst.TAB_SPECTRUM_RO[1];
            selectField2 = ConfigConst.TAB_SPECTRUM_RO[2];
        } else {
            selectField0 = ConfigConst.TAB_SPECTRUM_RW[0];
            selectField1 = ConfigConst.TAB_SPECTRUM_RW[1];
            selectField2 = ConfigConst.TAB_SPECTRUM_RW[2];
            selectField3 = ConfigConst.TAB_SPECTRUM_RW[3];
        }
        selectFields = selectField0 + ", " + selectField1 + ", " + selectField2;
        orderField = roFields ? ConfigConst.TAB_SPECTRUM_RO[0] : ConfigConst.TAB_SPECTRUM_RW[0];
        final String whereClause = "rownum" + " <= " + number + " ORDER BY  " + orderField + " ASC";
        final String query1 = "SELECT * FROM " + tableName + " ORDER BY " + selectField0 + " DESC";
        tableName = dbConn.getSchema() + "." + dbUtils.getTableName(attributeName) + " T";
        if (roFields) {
            selectField0 = "T" + "." + ConfigConst.TAB_SPECTRUM_RO[0];
            selectField1 = "T" + "." + ConfigConst.TAB_SPECTRUM_RO[1];
            selectField2 = "T" + "." + ConfigConst.TAB_SPECTRUM_RO[2];
        } else {
            selectField0 = "T" + "." + ConfigConst.TAB_SPECTRUM_RW[0];
            selectField1 = "T" + "." + ConfigConst.TAB_SPECTRUM_RW[1];
            selectField2 = "T" + "." + ConfigConst.TAB_SPECTRUM_RW[2];
            selectField3 = "T" + "." + ConfigConst.TAB_SPECTRUM_RW[3];
        }
        selectFields = dbUtils.toDbTimeFieldString(selectField0) + ", " + selectField1 + ", " + selectField2;
        if (!roFields) {
            selectFields += ", " + selectField3;
        }
        final String query = "SELECT " + selectFields + " FROM (" + query1 + ") T WHERE " + whereClause;
        try {
            conn = dbConn.getConnection();
            stmt = conn.createStatement();
            rset = stmt.executeQuery(query);
            while (rset.next()) {
                final SpectrumEvent_RO spectrumEventRO = new SpectrumEvent_RO();
                final SpectrumEvent_RW spectrumEventRW = new SpectrumEvent_RW();
                try {
                    spectrumEventRO.setTimeStamp(DateUtil.stringToMilli(rset.getString(1)));
                    spectrumEventRW.setTimeStamp(DateUtil.stringToMilli(rset.getString(1)));
                } catch (final Exception e) {
                }
                final int dimX = rset.getInt(2);
                spectrumEventRO.setDim_x(dimX);
                spectrumEventRW.setDim_x(dimX);
                Clob readClob = null;
                String readString = null;
                readClob = rset.getClob(3);
                if (rset.wasNull()) {
                    readString = "null";
                } else {
                    readString = readClob.getSubString(1, (int) readClob.length());
                }
                Clob writeClob = null;
                String writeString = null;
                if (!roFields) {
                    writeClob = rset.getClob(4);
                    if (rset.wasNull()) {
                        writeString = "null";
                    } else {
                        writeString = writeClob.getSubString(1, (int) writeClob.length());
                    }
                }
                final Object value = dbUtils.getSpectrumValue(readString, writeString, data_type);
                if (!roFields) {
                    spectrumEventRW.setValue(value);
                    tmpSpectrumVect.add(spectrumEventRW);
                } else {
                    spectrumEventRO.setValue(value);
                    tmpSpectrumVect.add(spectrumEventRO);
                }
            }
        } catch (final SQLException e) {
            throw new ArchivingException(e);
        } finally {
            ConnectionCommands.close(rset);
            ConnectionCommands.close(stmt);
            dbConn.closeConnection(conn);
        }
        return tmpSpectrumVect;
    }
