    private final boolean copy2Layouts() throws IOException, RecordException {
        int idx, lineNo;
        AbstractLineIOProvider ioProvider = LineIOProvider.getInstance();
        AbstractLineReader reader;
        AbstractLine in, next;
        ok = true;
        buildTranslations();
        reader = ioProvider.getLineReader(dtl1.getFileStructure());
        writer = ioProvider.getLineWriter(dtl2.getFileStructure());
        reader.open(cpy.oldFile.name, dtl1);
        writer.open(cpy.newFile.name);
        first = true;
        in = reader.read();
        lineNo = 0;
        if (dtl1.getRecordCount() < 2) {
            while (in != null) {
                writeRecord(in, (next = reader.read()), 0, lineNo++);
                in = next;
            }
        } else {
            while (in != null) {
                idx = in.getPreferredLayoutIdx();
                if (idx >= 0) {
                    while ((next = reader.read()) != null && next.getPreferredLayoutIdx() < 0) {
                    }
                    writeRecord(in, next, idx, lineNo++);
                    in = next;
                }
            }
        }
        reader.close();
        writer.close();
        closeFieldError();
        return ok;
    }
