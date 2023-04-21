    protected void retrieveCounterValues() throws Throwable {
        try {
            if (!fFile.exists()) {
                throw new RMSException("File " + fFile.getAbsolutePath() + " not found");
            }
            getCounter(new CounterId(Msg.COUNTER_SIZE_BYTES)).dataReceived(new CounterValueDouble(fFile.length()));
            getCounter(new CounterId(Msg.COUNTER_LAST_MODIFIED)).dataReceived(new CounterValueDouble(fFile.lastModified()));
            Counter counter = getCounter(new CounterId(Msg.COUNTER_LOG_RECORDS));
            if (counter.isEnabled()) {
                fInputStream = new RandomAccessFile(fFile, "r");
                long flen = fFile.length();
                if (fFirstCycle) {
                    fLastByteCount = flen;
                    fFirstCycle = false;
                } else {
                    if (fLastByteCount > flen) {
                        fLastByteCount = 0;
                    } else {
                    }
                }
                byte[] data = new byte[4096];
                ByteArrayOutputStream bytes = new ByteArrayOutputStream(4096);
                fInputStream.seek(fLastByteCount);
                int read;
                int readTotal = 0;
                double readMax = Math.pow(2, 20);
                while ((read = fInputStream.read(data)) > 0) {
                    fLastByteCount += read;
                    readTotal += read;
                    bytes.write(data, 0, read);
                    if (readTotal > readMax) {
                        break;
                    }
                }
                LinkedList<LogRecord> logRecords = new LinkedList<LogRecord>();
                String txt = bytes.toString(fEncoding);
                BufferedReader reader = new BufferedReader(new StringReader(txt));
                List<String> lines = new LinkedList<String>();
                String line;
                boolean recordStarted = false;
                boolean recordComplete = false;
                while ((line = reader.readLine()) != null) {
                    if (line.length() == 0) {
                        continue;
                    }
                    boolean recordBeginLine = false;
                    if (fLogRecordBeginRegex.matcher(line).find(0)) {
                        if (recordStarted || recordComplete) {
                            LogRecord record = createLogRecord(lines);
                            if (record != null) {
                                logRecords.add(record);
                            }
                            lines.clear();
                        }
                        recordBeginLine = true;
                        recordStarted = true;
                        lines.add(line);
                    } else {
                        if (recordStarted) {
                            lines.add(line);
                        }
                    }
                    if (fLogRecordEndRegex != null) {
                        String testForRecordEnd = line;
                        if (recordBeginLine) {
                            testForRecordEnd = line.substring(fRecordBeginRegex.length());
                        }
                        if (fLogRecordEndRegex.matcher(testForRecordEnd).find()) {
                            recordComplete = true;
                            recordStarted = false;
                        }
                    }
                }
                if (!Utils.isEmptyCollection(lines)) {
                    LogRecord record = createLogRecord(lines);
                    if (record != null) {
                        logRecords.add(record);
                    }
                }
                counter.dataReceived(new CounterValueObject(new LogRecordBatch(logRecords)));
            }
        } finally {
            if (fInputStream != null) {
                fInputStream.close();
            }
        }
    }
