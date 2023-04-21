    @Override
    public Vector getAttSpectrumDataBetweenDates(final String attributeName, final String time0, final String time1, final int dataType, final SamplingType samplingType) throws ArchivingException {
        final IDBConnection dbConn = ConnectionFactory.getInstance(archType);
        final IDbUtils dbUtils = DbUtilsFactory.getInstance(archType);
        final IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(archType);
        if (dbConn == null || dbUtils == null || att == null) {
            return null;
        }
        final Vector spectrumS = new Vector();
        final int writable = att.getAttDataWritable(attributeName);
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        final String query;
        final String selectField0;
        final String selectField1;
        final String selectField2;
        String selectField3 = "";
        String selectFields;
        final String dateClause;
        final String whereClause;
        final String groupByClause;
        final String orderByClause;
        final String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(attributeName) + " T";
        final boolean isBothReadAndWrite = !(writable == AttrWriteType._READ || writable == AttrWriteType._WRITE);
        if (samplingType.hasSampling()) {
            final String groupingNormalisationType = "MIN";
            if (!samplingType.hasAdditionalFiltering()) {
                final String format = samplingType.getOracleFormat();
                if (!isBothReadAndWrite) {
                    selectField0 = dbUtils.toDbTimeFieldString("T" + "." + ConfigConst.TAB_SPECTRUM_RO[0], format);
                    selectField1 = "AVG (" + "T" + "." + ConfigConst.TAB_SPECTRUM_RO[1] + ")";
                    selectField2 = "to_clob ( " + groupingNormalisationType + " ( to_char (" + "T" + "." + ConfigConst.TAB_SPECTRUM_RO[2] + ") ) )";
                } else {
                    selectField0 = dbUtils.toDbTimeFieldString("T" + "." + ConfigConst.TAB_SPECTRUM_RW[0], format);
                    selectField1 = "AVG (" + "T" + "." + ConfigConst.TAB_SPECTRUM_RW[1] + ")";
                    selectField2 = "to_clob ( " + groupingNormalisationType + " ( to_char (" + "T" + "." + ConfigConst.TAB_SPECTRUM_RW[2] + ") ) )";
                    selectField3 = "to_clob ( " + groupingNormalisationType + " ( to_char (" + "T" + "." + ConfigConst.TAB_SPECTRUM_RW[3] + ") ) )";
                }
                selectFields = selectField0 + ", " + selectField1 + ", " + selectField2;
                if (isBothReadAndWrite) {
                    selectFields += ", " + selectField3;
                }
                whereClause = ConfigConst.TAB_SPECTRUM_RO[0] + " BETWEEN " + dbUtils.toDbTimeString(time0.trim()) + " AND " + dbUtils.toDbTimeString(time1.trim());
                orderByClause = " ORDER BY " + dbUtils.toDbTimeFieldString(ConfigConst.TAB_SPECTRUM_RO[0], format);
                groupByClause = " GROUP BY " + dbUtils.toDbTimeFieldString(ConfigConst.TAB_SPECTRUM_RO[0], format);
                query = "SELECT " + selectFields + " FROM " + tableName + " WHERE " + whereClause + groupByClause + orderByClause;
            } else {
                final String format = samplingType.getOneLevelHigherFormat(false);
                final String fullFormat = SamplingType.getSamplingType(SamplingType.SECOND).getFormat(false);
                final String minTime = "MIN(" + ConfigConst.TAB_SCALAR_RO[0] + ")";
                selectField0 = dbUtils.toDbTimeFieldString(minTime, fullFormat);
                if (!isBothReadAndWrite) {
                    selectField1 = "AVG (" + "T" + "." + ConfigConst.TAB_SPECTRUM_RO[1] + ")";
                    selectField2 = "to_clob ( " + groupingNormalisationType + " ( to_char (" + "T" + "." + ConfigConst.TAB_SPECTRUM_RO[2] + ") ) )";
                } else {
                    selectField1 = "AVG (" + "T" + "." + ConfigConst.TAB_SPECTRUM_RW[1] + ")";
                    selectField2 = "to_clob ( " + groupingNormalisationType + " ( to_char (" + "T" + "." + ConfigConst.TAB_SPECTRUM_RW[2] + ") ) )";
                    selectField3 = "to_clob ( " + groupingNormalisationType + " ( to_char (" + "T" + "." + ConfigConst.TAB_SPECTRUM_RW[3] + ") ) )";
                }
                selectFields = selectField0 + ", " + selectField1 + ", " + selectField2;
                if (isBothReadAndWrite) {
                    selectFields += ", " + selectField3;
                }
                whereClause = ConfigConst.TAB_SPECTRUM_RO[0] + " BETWEEN " + dbUtils.toDbTimeString(time0.trim()) + " AND " + dbUtils.toDbTimeString(time1.trim());
                groupByClause = " GROUP BY " + dbUtils.toDbTimeFieldString(ConfigConst.TAB_SCALAR_RO[0], format) + samplingType.getAdditionalFilteringClause(false, ConfigConst.TAB_SCALAR_RO[0]);
                orderByClause = " ORDER BY " + "MIN(" + ConfigConst.TAB_SCALAR_RW[0] + ")";
                query = "SELECT " + selectFields + " FROM " + tableName + " WHERE " + whereClause + groupByClause + orderByClause;
            }
        } else {
            if (!isBothReadAndWrite) {
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
            if (isBothReadAndWrite) {
                selectFields += ", " + selectField3;
            }
            dateClause = selectField0 + " BETWEEN " + dbUtils.toDbTimeString(time0.trim()) + " AND " + dbUtils.toDbTimeString(time1.trim());
            query = "SELECT " + selectFields + " FROM " + tableName + " WHERE " + "(" + dateClause + ")" + " ORDER BY time";
        }
        try {
            conn = dbConn.getConnection();
            stmt = conn.createStatement();
            rset = stmt.executeQuery(query);
            while (rset.next()) {
                final SpectrumEvent_RO spectrumEventRO = new SpectrumEvent_RO();
                final SpectrumEvent_RW spectrumEventRW = new SpectrumEvent_RW();
                spectrumEventRO.setTimeStamp(DateUtil.stringToMilli(rset.getString(1)));
                spectrumEventRW.setTimeStamp(DateUtil.stringToMilli(rset.getString(1)));
                final int dimX = rset.getInt(2);
                spectrumEventRO.setDim_x(dimX);
                spectrumEventRW.setDim_x(dimX);
                final String readString;
                final Clob readClob = rset.getClob(3);
                if (rset.wasNull()) {
                    readString = "null";
                } else {
                    readString = readClob.getSubString(1, (int) readClob.length());
                }
                Clob writeClob = null;
                String writeString = null;
                if (isBothReadAndWrite) {
                    writeClob = rset.getClob(4);
                    if (rset.wasNull()) {
                        writeString = "null";
                    } else {
                        writeString = writeClob.getSubString(1, (int) writeClob.length());
                    }
                }
                final Object value = dbUtils.getSpectrumValue(readString, writeString, dataType);
                if (isBothReadAndWrite) {
                    spectrumEventRW.setValue(value);
                    spectrumS.add(spectrumEventRW);
                } else {
                    spectrumEventRO.setValue(value);
                    spectrumS.add(spectrumEventRO);
                }
            }
        } catch (final SQLException e) {
            throw new ArchivingException(e);
        } finally {
            ConnectionCommands.close(rset);
            ConnectionCommands.close(stmt);
            dbConn.closeConnection(conn);
        }
        return spectrumS;
    }
