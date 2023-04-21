    private void check_header(BinlogPosition position) throws MySQLExtractException {
        byte header[] = new byte[MysqlBinlog.BIN_LOG_HEADER_SIZE];
        byte buf[] = new byte[MysqlBinlog.PROBE_HEADER_LEN];
        long tmp_pos = 0;
        FormatDescriptionLogEvent description_event = new FormatDescriptionLogEvent(3);
        try {
            if (position.getFis().read(header) != header.length) {
                logger.error("Failed reading header;  Probably an empty file");
                throw new MySQLExtractException("could not read binlog header");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if (!java.util.Arrays.equals(header, MysqlBinlog.BINLOG_MAGIC)) {
            logger.error("File is not a binary log file");
            throw new MySQLExtractException("binlog file header mismatch");
        }
        for (; ; ) {
            try {
                tmp_pos = position.getFis().getChannel().position();
                position.getFis().mark(2048);
                position.getDis().readFully(buf);
                logger.debug("buf[4]=" + buf[4]);
                long start_position = 0;
                if (buf[4] == MysqlBinlog.START_EVENT_V3) {
                    if (LittleEndianConversion.convert4BytesToLong(buf, MysqlBinlog.EVENT_LEN_OFFSET) < (MysqlBinlog.LOG_EVENT_MINIMAL_HEADER_LEN + MysqlBinlog.START_V3_HEADER_LEN)) {
                        description_event = new FormatDescriptionLogEvent(1);
                    }
                    break;
                } else if (tmp_pos >= start_position) break; else if (buf[4] == MysqlBinlog.FORMAT_DESCRIPTION_EVENT) {
                    FormatDescriptionLogEvent new_description_event;
                    position.getFis().reset();
                    new_description_event = (FormatDescriptionLogEvent) LogEvent.readLogEvent(runtime, position, description_event, parseStatements, useBytesForStrings, prefetchSchemaNameLDI);
                    if (new_description_event == null) {
                        logger.error("Could not read a Format_description_log_event event " + "at offset " + tmp_pos + "this could be a log format error or read error");
                        throw new MySQLExtractException("binlog format error");
                    }
                    description_event = new_description_event;
                    logger.debug("Setting description_event");
                } else if (buf[4] == MysqlBinlog.ROTATE_EVENT) {
                    LogEvent ev;
                    position.getFis().reset();
                    ev = LogEvent.readLogEvent(runtime, position, description_event, parseStatements, useBytesForStrings, prefetchSchemaNameLDI);
                    if (ev == null) {
                        logger.error("Could not read a Rotate_log_event event " + "at offset " + tmp_pos + " this could be a log format error or " + "read error");
                        throw new MySQLExtractException("binlog format error");
                    }
                } else {
                    break;
                }
            } catch (EOFException e) {
                break;
            } catch (IOException e) {
                logger.error("Could not read entry at offset " + tmp_pos + " : Error in log format or read error");
                throw new MySQLExtractException("binlog read error" + e);
            }
        }
    }
