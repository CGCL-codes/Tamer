    public int writeRecords(InputStreamReader reader, SingleTapeWriter tapewriter) throws Exception {
        if (!initialized) init();
        char[] buffer = new char[INITIAL_BUFFER_SIZE];
        int count = 0;
        CharArrayWriter charbuffer = new CharArrayWriter(INITIAL_BUFFER_SIZE);
        int recordend = 0;
        while ((count = reader.read(buffer)) != -1) {
            charbuffer.write(buffer, 0, count);
            String record = null;
            String records = charbuffer.toString();
            Matcher recordmatch_matcher = recordmatch_pattern.matcher(records);
            while (recordmatch_matcher.find()) {
                record = recordmatch_matcher.group();
                tapewriter.writeRecord(buildRecord(record));
                sum_records++;
                recordend = recordmatch_matcher.end();
            }
            if (recordend != 0) {
                charbuffer.reset();
                charbuffer.write(records.substring(recordend));
                recordend = 0;
            }
        }
        return sum_records;
    }
