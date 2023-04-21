    public FixedColumnsParser setFixedColumnsParser(Reader in, String delim) throws IOException {
        BufferedReader reader = new LineNumberReader(in);
        String line;
        line = readFirstRecord(reader);
        reader.close();
        int[] columnOffsets;
        int[] columnWidths;
        int col = 0;
        String[] ss = line.split(delim);
        columnOffsets = new int[ss.length];
        columnWidths = new int[ss.length - 1];
        fieldCount = ss.length;
        fieldParsers = new FieldParser[ss.length - 1];
        boolean rightJustified = false;
        if (ss[0].trim().length() == 0) {
            rightJustified = true;
            for (int i = 0; i < ss.length - 1; i++) {
                ss[i] = ss[i + 1];
            }
        }
        columnOffsets[0] = 0;
        if (rightJustified) {
            for (int i = 1; i < ss.length; i++) {
                col = line.indexOf(ss[i - 1], columnOffsets[i - 1]);
                columnOffsets[i] = col + ss[i - 1].length();
                columnWidths[i - 1] = columnOffsets[i] - columnOffsets[i - 1];
            }
        } else {
            for (int i = 1; i < ss.length; i++) {
                col = line.indexOf(ss[i], col + ss[i - 1].length());
                columnOffsets[i] = col;
                columnWidths[i - 1] = columnOffsets[i] - columnOffsets[i - 1];
            }
        }
        fieldNames = new String[fieldCount];
        for (int i = 1; i < ss.length; i++) {
            fieldParsers[i - 1] = DOUBLE_PARSER;
            fieldNames[i - 1] = "field" + (i - 1);
        }
        int[] co = new int[columnWidths.length];
        System.arraycopy(columnOffsets, 0, co, 0, columnWidths.length);
        this.units = new Units[fieldCount];
        for (int i = 0; i < fieldCount; i++) {
            units[i] = Units.dimensionless;
        }
        FixedColumnsParser p = new FixedColumnsParser(co, columnWidths);
        this.recordParser = p;
        this.propertyPattern = null;
        return p;
    }
