        public void publish(LogRecord record) {
            if (!isLoggable(record)) {
                return;
            }
            try {
                StringBuffer message = new StringBuffer(record.getMessage());
                if (record.getThrown() != null) {
                    message.append("\n");
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    record.getThrown().printStackTrace(pw);
                    pw.close();
                    message.append(sw.toString());
                }
                write(encode(record.getLoggerName(), message.toString(), record.getSourceClassName(), record.getSourceMethodName(), record.getLevel().intValue(), record.getMillis(), record.getSequenceNumber(), record.getThreadID()));
            } catch (IOException e) {
                getErrorManager().error("Can not publish", e, ErrorManager.WRITE_FAILURE);
                close();
            }
        }
