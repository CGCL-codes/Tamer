    private DDataSet doReadFile(final ProgressMonitor mon) throws NumberFormatException, IOException, FileNotFoundException {
        String o;
        file = getFile(mon);
        if (file.isDirectory()) {
            throw new IOException("expected file but got directory");
        }
        parser = new AsciiParser();
        boolean fixedColumns = false;
        int columnCount = 0;
        String delim;
        o = params.get("skip");
        if (o != null) {
            parser.setSkipLines(Integer.parseInt(o));
        }
        o = params.get("skipLines");
        if (o != null) {
            parser.setSkipLines(Integer.parseInt(o));
        }
        o = params.get("recCount");
        if (o != null) {
            parser.setRecordCountLimit(Integer.parseInt(o));
        }
        parser.setKeepFileHeader(true);
        o = params.get("comment");
        if (o != null) {
            if (o.equals("")) {
                parser.setCommentPrefix(null);
            } else {
                parser.setCommentPrefix(o);
            }
        }
        o = params.get("headerDelim");
        if (o != null) {
            parser.setHeaderDelimiter(o);
        }
        delim = params.get("delim");
        String sFixedColumns = params.get("fixedColumns");
        if (sFixedColumns == null) {
            if (delim == null) {
                AsciiParser.DelimParser p = parser.guessSkipAndDelimParser(file.toString());
                if (p == null) {
                    throw new IllegalArgumentException("no records found");
                }
                columnCount = p.fieldCount();
                delim = p.getDelim();
            } else {
                if (delim.equals(",")) delim = "COMMA";
                delim = delim.replaceAll("WHITESPACE", "\\s+");
                delim = delim.replaceAll("SPACE", " ");
                delim = delim.replaceAll("COMMA", ",");
                delim = delim.replaceAll("COLON", ":");
                delim = delim.replaceAll("TAB", "\t");
                delim = delim.replaceAll("whitespace", "\\s+");
                delim = delim.replaceAll("space", " ");
                delim = delim.replaceAll("comma", ",");
                delim = delim.replaceAll("colon", ":");
                delim = delim.replaceAll("tab", "\t");
                if (delim.equals("+")) {
                    delim = " ";
                }
                columnCount = parser.setDelimParser(file.toString(), delim).fieldCount();
            }
            parser.setPropertyPattern(AsciiParser.NAME_COLON_VALUE_PATTERN);
        } else {
            String s = sFixedColumns;
            AsciiParser.RecordParser p = parser.setFixedColumnsParser(file.toString(), "\\s+");
            try {
                columnCount = Integer.parseInt(sFixedColumns);
            } catch (NumberFormatException ex) {
                if (sFixedColumns.equals("")) {
                    columnCount = p.fieldCount();
                } else {
                    String[] ss = s.split(",");
                    int[] starts = new int[ss.length];
                    int[] widths = new int[ss.length];
                    AsciiParser.FieldParser[] fparsers = new AsciiParser.FieldParser[ss.length];
                    for (int i = 0; i < ss.length; i++) {
                        String[] ss2 = ss[i].split("-");
                        starts[i] = Integer.parseInt(ss2[0]);
                        widths[i] = Integer.parseInt(ss2[1]) - starts[i] + 1;
                        fparsers[i] = AsciiParser.DOUBLE_PARSER;
                    }
                    p = parser.setFixedColumnsParser(starts, widths, fparsers);
                    columnCount = p.fieldCount();
                }
            }
            parser.setPropertyPattern(null);
            fixedColumns = true;
            delim = null;
        }
        o = params.get("columnCount");
        if (columnCount == 0) {
            if (o != null) {
                columnCount = Integer.parseInt(o);
            } else {
                columnCount = AsciiParser.guessFieldCount(file.toString());
            }
        }
        o = params.get("fill");
        if (o != null) {
            parser.setFillValue(Double.parseDouble(o));
        }
        o = params.get("validMin");
        if (o != null) {
            this.validMin = Double.parseDouble(o);
        }
        o = params.get("validMax");
        if (o != null) {
            this.validMax = Double.parseDouble(o);
        }
        o = params.get("time");
        if (o != null) {
            int i = parser.getFieldIndex(o);
            if (i == -1) {
                throw new IllegalArgumentException("field not found for time in column named \"" + o + "\"");
            } else {
                parser.setFieldParser(i, parser.UNITS_PARSER);
                parser.setUnits(i, Units.t2000);
                depend0 = o;
                timeColumn = i;
            }
        }
        o = params.get("timeFormat");
        if (o != null) {
            String timeFormat = o.replaceAll("\\+", " ");
            timeFormat = timeFormat.replaceAll("\\$", "%");
            timeFormat = timeFormat.replaceAll("\\(", "{");
            timeFormat = timeFormat.replaceAll("\\)", "}");
            String timeColumnName = params.get("time");
            timeColumn = timeColumnName == null ? 0 : parser.getFieldIndex(timeColumnName);
            String timeFormatDelim = delim;
            if (delim == null) timeFormatDelim = " ";
            timeFormats = timeFormat.split(timeFormatDelim, -2);
            if (timeFormats.length == 1) {
                timeFormatDelim = " ";
                timeFormats = timeFormat.split(timeFormatDelim, -2);
            }
            if (timeFormat.equals("ISO8601")) {
                String line = parser.readFirstParseableRecord(file.toString());
                if (line == null) {
                    throw new IllegalArgumentException("file contains no parseable records.");
                }
                String[] ss = new String[parser.getRecordParser().fieldCount()];
                parser.getRecordParser().splitRecord(line, ss);
                int i = timeColumn;
                if (i == -1) {
                    i = 0;
                }
                String atime = ss[i];
                timeFormat = TimeParser.iso8601String(atime.trim());
                timeParser = TimeParser.create(timeFormat);
                final Units u = Units.t2000;
                parser.setUnits(i, u);
                AsciiParser.FieldParser timeFieldParser = new AsciiParser.FieldParser() {

                    public double parseField(String field, int fieldIndex) throws ParseException {
                        return timeParser.parse(field).getTime(u);
                    }
                };
                parser.setFieldParser(i, timeFieldParser);
            } else if (delim != null && timeFormats.length > 1) {
                timeParser = TimeParser.create(timeFormat);
                parser.setUnits(timeColumn, Units.dimensionless);
                final Units u = Units.t2000;
                MultiFieldTimeParser timeFieldParser = new MultiFieldTimeParser(timeColumn, timeFormats, timeParser, u);
                for (int i = timeColumn; i < timeColumn + timeFormats.length; i++) {
                    parser.setFieldParser(i, timeFieldParser);
                    parser.setUnits(i, Units.dimensionless);
                }
                timeColumn = timeColumn + timeFormats.length - 1;
                if (params.get("time") != null) {
                    depend0 = parser.getFieldNames()[timeColumn];
                }
                parser.setUnits(timeColumn, u);
            } else {
                timeParser = TimeParser.create(timeFormat);
                final Units u = Units.t2000;
                parser.setUnits(timeColumn, u);
                AsciiParser.FieldParser timeFieldParser = new AsciiParser.FieldParser() {

                    public double parseField(String field, int fieldIndex) throws ParseException {
                        return timeParser.parse(field).getTime(u);
                    }
                };
                parser.setFieldParser(timeColumn, timeFieldParser);
            }
        } else {
            timeParser = null;
        }
        o = params.get("depend0");
        if (o != null) {
            depend0 = o;
        }
        o = params.get("column");
        if (o != null) {
            column = o;
        }
        o = params.get("rank2");
        if (o != null) {
            rank2 = parseRangeStr(o, columnCount);
            column = null;
        }
        o = params.get("bundle");
        if (o != null) {
            bundle = parseRangeStr(o, columnCount);
            column = null;
        }
        o = params.get("arg_0");
        if (o != null) {
            if (o.equals("rank2")) {
                rank2 = new int[] { 0, columnCount };
                column = null;
            } else if (o.equals("bundle")) {
                bundle = new int[] { 0, columnCount };
                column = null;
            }
        }
        if (column == null && depend0 == null && rank2 == null) {
            if (parser.getFieldNames().length == 2) {
                depend0 = parser.getFieldNames()[0];
                column = parser.getFieldNames()[1];
            } else {
                column = parser.getFieldNames()[0];
            }
        }
        o = params.get("depend1Labels");
        if (o != null) {
            if (o.contains(",")) {
                depend1Label = o.split(",");
            } else {
                depend1Labels = parseRangeStr(o, columnCount);
            }
        }
        o = params.get("depend1Values");
        if (o != null) {
            depend1Values = parseRangeStr(o, columnCount);
        }
        if (timeColumn == -1) {
            String s = parser.readFirstParseableRecord(file.toString());
            if (s != null) {
                String[] fields = new String[parser.getRecordParser().fieldCount()];
                parser.getRecordParser().splitRecord(s, fields);
                if (depend0 != null) {
                    int idep0 = parser.getFieldIndex(depend0);
                    if (idep0 != -1) {
                        String field = fields[idep0];
                        try {
                            TimeUtil.parseTime(field);
                            if (new StringTokenizer(field, ":T-/").countTokens() > 1) {
                                parser.setUnits(idep0, Units.us2000);
                                parser.setFieldParser(idep0, parser.UNITS_PARSER);
                            }
                        } catch (ParseException ex) {
                        }
                    }
                }
                if (column != null) {
                    int icol = parser.getFieldIndex(column);
                    if (icol != -1) {
                        String field = fields[icol];
                        try {
                            TimeUtil.parseTime(field);
                            if (new StringTokenizer(field, ":T-/").countTokens() > 1) {
                                parser.setUnits(icol, Units.us2000);
                                parser.setFieldParser(icol, parser.UNITS_PARSER);
                            }
                        } catch (ParseException ex) {
                        }
                    }
                }
                for (int icol = 0; icol < fields.length && icol < 2; icol++) {
                    String field = fields[icol];
                    try {
                        if (new StringTokenizer(field, ":T-/").countTokens() > 1) {
                            TimeUtil.parseTime(field);
                            parser.setUnits(icol, Units.us2000);
                            parser.setFieldParser(icol, parser.UNITS_PARSER);
                        }
                    } catch (ParseException ex) {
                    }
                }
            }
        }
        o = params.get("units");
        if (o != null) {
            String sunits = o;
            Units u = SemanticOps.lookupUnits(sunits);
            if (column != null) {
                int icol = parser.getFieldIndex(column);
                parser.setUnits(icol, u);
                parser.setFieldParser(icol, parser.UNITS_PARSER);
            }
        }
        o = params.get("eventListColumn");
        if (o != null) {
            parser.setFieldParser(0, parser.UNITS_PARSER);
            parser.setFieldParser(1, parser.UNITS_PARSER);
            int icol = parser.getFieldIndex(o);
            EnumerationUnits eu = EnumerationUnits.create("events");
            parser.setUnits(icol, eu);
            parser.setFieldParser(icol, parser.ENUMERATION_PARSER);
        }
        DDataSet ds1;
        o = params.get("tail");
        if (o != null) {
            ByteBuffer buff = new FileInputStream(file).getChannel().map(MapMode.READ_ONLY, 0, file.length());
            int tailNum = Integer.parseInt(o);
            int tailCount = 0;
            int ipos = (int) file.length();
            boolean foundNonEOL = false;
            while (tailCount < tailNum && ipos > 0) {
                ipos--;
                byte ch = buff.get((int) ipos);
                if (ch == 10) {
                    if (ipos > 1 && buff.get(ipos - 1) == 13) ipos = ipos - 1;
                    if (foundNonEOL) tailCount++;
                } else if (ch == 13) {
                    if (foundNonEOL) tailCount++;
                } else {
                    foundNonEOL = true;
                }
            }
            buff.position(tailCount < tailNum ? 0 : ipos + 1);
            InputStream in = new ByteBufferInputStream(buff);
            ds1 = (DDataSet) parser.readStream(new InputStreamReader(in), mon);
        } else {
            ds1 = (DDataSet) parser.readFile(file.toString(), mon);
        }
        return ds1;
    }
