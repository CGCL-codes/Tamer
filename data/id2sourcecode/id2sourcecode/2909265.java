    public static LogEvent readLogEvent(ReplicatorRuntime runtime, BinlogPosition position, FormatDescriptionLogEvent descriptionEvent, boolean parseStatements, boolean useBytesForString, boolean prefetchSchemaNameLDI) throws MySQLExtractException {
        DataInputStream dis = position.getDis();
        int eventLength = 0;
        byte[] header = new byte[descriptionEvent.commonHeaderLength];
        try {
            readDataFromBinlog(runtime, dis, header, 0, header.length, 60, ReplicatorMonitor.REAL_EXTHEAD);
            eventLength = (int) LittleEndianConversion.convert4BytesToLong(header, MysqlBinlog.EVENT_LEN_OFFSET);
            eventLength -= header.length;
            byte[] fullEvent = new byte[header.length + eventLength];
            readDataFromBinlog(runtime, dis, fullEvent, header.length, eventLength, 120, ReplicatorMonitor.REAL_EXTBODY);
            System.arraycopy(header, 0, fullEvent, 0, header.length);
            LogEvent event = readLogEvent(parseStatements, fullEvent, fullEvent.length, descriptionEvent, useBytesForString);
            if (prefetchSchemaNameLDI && event instanceof BeginLoadQueryLogEvent) {
                if (logger.isDebugEnabled()) logger.debug("Got Begin Load Query Event - Looking for corresponding Execute Event");
                BeginLoadQueryLogEvent beginLoadEvent = (BeginLoadQueryLogEvent) event;
                BinlogPosition tempPosition = position.clone();
                tempPosition.setEventID(position.getEventID() + 1);
                tempPosition.setPosition((int) position.getFis().getChannel().position());
                tempPosition.openFile();
                if (logger.isDebugEnabled()) logger.debug("Reading from " + tempPosition);
                boolean found = false;
                byte[] tmpHeader = new byte[descriptionEvent.commonHeaderLength];
                while (!found) {
                    readDataFromBinlog(runtime, tempPosition.getDis(), tmpHeader, 0, tmpHeader.length, 60, ReplicatorMonitor.REAL_EXTHEAD);
                    eventLength = (int) LittleEndianConversion.convert4BytesToLong(tmpHeader, MysqlBinlog.EVENT_LEN_OFFSET) - tmpHeader.length;
                    if (tmpHeader[MysqlBinlog.EVENT_TYPE_OFFSET] == MysqlBinlog.EXECUTE_LOAD_QUERY_EVENT) {
                        fullEvent = new byte[tmpHeader.length + eventLength];
                        readDataFromBinlog(runtime, tempPosition.getDis(), fullEvent, tmpHeader.length, eventLength, 120, ReplicatorMonitor.REAL_EXTBODY);
                        System.arraycopy(tmpHeader, 0, fullEvent, 0, tmpHeader.length);
                        LogEvent tempEvent = readLogEvent(parseStatements, fullEvent, fullEvent.length, descriptionEvent, useBytesForString);
                        if (tempEvent instanceof ExecuteLoadQueryLogEvent) {
                            ExecuteLoadQueryLogEvent execLoadQueryEvent = (ExecuteLoadQueryLogEvent) tempEvent;
                            if (execLoadQueryEvent.getFileID() == beginLoadEvent.getFileID()) {
                                if (logger.isDebugEnabled()) logger.debug("Found corresponding Execute Load Query Event - Schema is " + execLoadQueryEvent.getDefaultDb());
                                beginLoadEvent.setSchemaName(execLoadQueryEvent.getDefaultDb());
                                found = true;
                            }
                        }
                    } else {
                        long skip = 0;
                        while (skip != eventLength) {
                            skip += tempPosition.getDis().skip(eventLength - skip);
                        }
                    }
                }
                tempPosition.reset();
            }
            return event;
        } catch (EOFException e) {
            throw new MySQLExtractException("EOFException while reading " + eventLength + " bytes from binlog ", e);
        } catch (IOException e) {
            throw new MySQLExtractException("binlog read error", e);
        }
    }
