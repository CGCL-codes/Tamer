    private LogEvent processFile(BinlogPosition position) throws ExtractorException, InterruptedException {
        try {
            if (position.getFis() == null) {
                position.openFile();
            }
            logger.debug("extracting from pos, file: " + position.getFileName() + " pos: " + position.getPosition());
            if (position.getFis() != null) {
                long indexCheckStart = System.currentTimeMillis();
                while (position.getDis().available() == 0) {
                    if (System.currentTimeMillis() - indexCheckStart > INDEX_CHECK_INTERVAL) {
                        BinlogIndex bi = new BinlogIndex(binlogDir, binlogFilePattern, true);
                        File nextBinlog = bi.nextBinlog(position.getFileName());
                        if (nextBinlog != null) {
                            logger.warn("Current log file appears to be missing log-rotate event: " + position.getFileName());
                            logger.info("Auto-generating log-rotate event for next binlog file: " + nextBinlog.getName());
                            return new RotateLogEvent(nextBinlog.getName());
                        }
                        assertRelayLogsEnabled();
                        indexCheckStart = System.currentTimeMillis();
                    }
                    Thread.sleep(10);
                }
                FormatDescriptionLogEvent description_event = new FormatDescriptionLogEvent(4);
                if (position.getPosition() == 0) {
                    check_header(position);
                    position.setPosition(0);
                    position.openFile();
                    byte[] buf = new byte[MysqlBinlog.BIN_LOG_HEADER_SIZE];
                    position.getDis().readFully(buf);
                }
                LogEvent event = LogEvent.readLogEvent(runtime, position, description_event, parseStatements, useBytesForStrings, prefetchSchemaNameLDI);
                position.setEventID(position.getEventID() + 1);
                position.setPosition((int) position.getFis().getChannel().position());
                return event;
            } else {
                logger.error("binlog file channel not open");
                throw new MySQLExtractException("binlog file channel not open");
            }
        } catch (IOException e) {
            logger.error("binlog file read error");
            throw new MySQLExtractException("binlog file read error");
        }
    }
