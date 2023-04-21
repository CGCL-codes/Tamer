    @Override
    protected Vector treatStatementResultForGetSpectData(final ResultSet rset, final boolean isBothReadAndWrite, final int dataType, final Vector spectrumS) throws ArchivingException {
        final IDbUtils dbUtils = DbUtilsFactory.getInstance(archType);
        if (dbUtils == null) {
            return null;
        }
        try {
            while (rset.next()) {
                final SpectrumEvent_RO spectrumEventRO = new SpectrumEvent_RO();
                final SpectrumEvent_RW spectrumEventRW = new SpectrumEvent_RW();
                final String rawDate = rset.getString(1);
                final long milliDate = DateUtil.stringToMilli(rawDate);
                spectrumEventRO.setTimeStamp(milliDate);
                spectrumEventRW.setTimeStamp(milliDate);
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
        }
        return spectrumS;
    }
