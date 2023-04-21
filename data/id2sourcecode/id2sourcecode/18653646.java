    public synchronized void processEventScalar(ScalarEvent scalarEvent) throws ArchivingException {
        try {
            String readValue = scalarEvent.valueToString(0);
            String writeValue = scalarEvent.valueToString(1);
            if (_dbProxy.getDataBase().getDbConn().getDbType() == ConfigConst.TDB_ORACLE) {
                if (readValue == null || GlobalConst.ARCHIVER_NULL_VALUE.equalsIgnoreCase(readValue.trim())) {
                    readValue = GlobalConst.ORACLE_NULL_VALUE;
                }
                if (writeValue == null || GlobalConst.ARCHIVER_NULL_VALUE.equalsIgnoreCase(writeValue.trim())) {
                    writeValue = GlobalConst.ORACLE_NULL_VALUE;
                }
                if (scalarEvent.getData_type() == TangoConst.Tango_DEV_STRING) {
                    readValue = StringFormater.formatStringToWrite(readValue);
                    writeValue = StringFormater.formatStringToWrite(writeValue);
                }
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("\"");
                stringBuffer.append(DateUtil.milliToString(scalarEvent.getTimeStamp(), DateUtil.FR_DATE_PATTERN));
                stringBuffer.append("\"");
                stringBuffer.append(",");
                switch(scalarEvent.getWritable()) {
                    case AttrWriteType._READ:
                        stringBuffer.append("\"").append(readValue).append("\"");
                        break;
                    case AttrWriteType._READ_WRITE:
                    case AttrWriteType._READ_WITH_WRITE:
                        stringBuffer.append("\"").append(readValue).append("\"");
                        stringBuffer.append(",");
                        stringBuffer.append("\"").append(writeValue).append("\"");
                        break;
                    case AttrWriteType._WRITE:
                        stringBuffer.append("\"").append(writeValue).append("\"");
                        break;
                }
                write(stringBuffer.toString());
                write(ConfigConst.NEW_LINE);
            } else if (_dbProxy.getDataBase().getDbConn().getDbType() == ConfigConst.TDB_MYSQL) {
                if (readValue == null || GlobalConst.ARCHIVER_NULL_VALUE.equalsIgnoreCase(readValue.trim())) {
                    readValue = GlobalConst.MYSQL_NULL_VALUE;
                }
                if (writeValue == null || GlobalConst.ARCHIVER_NULL_VALUE.equalsIgnoreCase(writeValue.trim())) {
                    writeValue = GlobalConst.MYSQL_NULL_VALUE;
                }
                if (scalarEvent.getData_type() == TangoConst.Tango_DEV_STRING) {
                    readValue = StringFormater.formatStringToWrite(readValue);
                    writeValue = StringFormater.formatStringToWrite(writeValue);
                }
                switch(scalarEvent.getWritable()) {
                    case AttrWriteType._READ:
                        write(new StringBuffer().append(toDbTimeStringMySQL(scalarEvent.getTimeStamp())).append(ConfigConst.FIELDS_LIMIT).append(readValue).append(ConfigConst.LINES_LIMIT).toString());
                        break;
                    case AttrWriteType._READ_WITH_WRITE:
                        write(new StringBuffer().append(toDbTimeStringMySQL(scalarEvent.getTimeStamp())).append(ConfigConst.FIELDS_LIMIT).append(readValue).append(ConfigConst.FIELDS_LIMIT).append(writeValue).append(ConfigConst.LINES_LIMIT).toString());
                        break;
                    case AttrWriteType._WRITE:
                        write(new StringBuffer().append(toDbTimeStringMySQL(scalarEvent.getTimeStamp())).append(ConfigConst.FIELDS_LIMIT).append(writeValue).append(ConfigConst.LINES_LIMIT).toString());
                        break;
                    case AttrWriteType._READ_WRITE:
                        write((new StringBuffer().append(toDbTimeStringMySQL(scalarEvent.getTimeStamp())).append(ConfigConst.FIELDS_LIMIT).append(readValue).append(ConfigConst.FIELDS_LIMIT).append(writeValue).append(ConfigConst.LINES_LIMIT)).toString());
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Util.out2.println("ERROR !! " + "\r\n" + "\t Origin : \t " + "FileTools.processEventScalar" + "\r\n" + "\t Reason : \t " + e.getClass().getName() + "\r\n" + "\t Description : \t " + e.getMessage() + "\r\n" + "\t Additional information : \t " + "File :\t " + _fileName + "\r\n");
            if (e.getMessage().indexOf("Stream closed") != -1) {
                openFileSimply();
            }
        }
    }
