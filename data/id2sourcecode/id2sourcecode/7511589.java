    public List parse(File controlFile) throws IOException, SqlLoaderControlParserException {
        logger.debug("parse(controlFile={}) - start", controlFile);
        FileInputStream fis = new FileInputStream(controlFile);
        FileChannel fc = fis.getChannel();
        MappedByteBuffer mbf = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        byte[] barray = new byte[(int) (fc.size())];
        mbf.get(barray);
        String lines = new String(barray);
        lines = lines.replaceAll("\r", "");
        if (parseForRegexp(lines, "(LOAD\\sDATA).*") != null) {
            String fileName = parseForRegexp(lines, ".*INFILE\\s'(.*?)'.*");
            File dataFile = resolveFile(controlFile.getParentFile(), fileName);
            this.tableName = parseForRegexp(lines, ".*INTO\\sTABLE\\s(.*?)\\s.*");
            if (parseForRegexp(lines, ".*(TRAILING NULLCOLS).*") != "") {
                this.hasTrailingNullCols = true;
            } else {
                this.hasTrailingNullCols = false;
            }
            List rows = new ArrayList();
            List columnList = parseColumns(lines, rows);
            LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(new FileInputStream(dataFile)));
            try {
                parseTheData(columnList, lineNumberReader, rows);
            } finally {
                lineNumberReader.close();
            }
            return rows;
        } else {
            throw new SqlLoaderControlParserException("Control file " + controlFile + " not starting using 'LOAD DATA'");
        }
    }
