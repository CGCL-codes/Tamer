    public boolean execute(CommandProcessor processor, Command command, boolean skip) throws SQLException {
        if (command.isTransient()) return false;
        if (!triggerPattern.matcher(command.getCommand()).matches()) return false;
        if (skip) return true;
        Parsed parsed = parse(command);
        Resource jsvResource = new FileResource(new File(parsed.fileName));
        JSONWriter jsonWriter = new JSONWriter(jsvResource);
        try {
            try {
                Statement statement = processor.createStatement();
                try {
                    ResultSet result = statement.executeQuery(parsed.query);
                    ResultSetMetaData metaData = result.getMetaData();
                    int columns = metaData.getColumnCount();
                    int[] types = new int[columns];
                    String[] names = new String[columns];
                    boolean[] ignore = new boolean[columns];
                    FileSpec[] fileSpecs = new FileSpec[columns];
                    String schemaNames[] = new String[columns];
                    String tableNames[] = new String[columns];
                    for (int i = 0; i < columns; i++) {
                        int col = i + 1;
                        String name = metaData.getColumnName(col).toUpperCase();
                        types[i] = metaData.getColumnType(col);
                        if (types[i] == Types.DATE && parsed.dateAsTimestamp) types[i] = Types.TIMESTAMP;
                        names[i] = name;
                        if (parsed.columns != null) fileSpecs[i] = parsed.columns.get(name);
                        if (parsed.coalesce != null && parsed.coalesce.notFirst(name)) ignore[i] = true; else if (types[i] == 2002 || JDBCSupport.toTypeName(types[i]) == null) ignore[i] = true;
                        tableNames[i] = StringUtils.upperCase(StringUtils.defaultIfEmpty(metaData.getTableName(col), null));
                        schemaNames[i] = StringUtils.upperCase(StringUtils.defaultIfEmpty(metaData.getSchemaName(col), null));
                    }
                    if (parsed.coalesce != null) parsed.coalesce.bind(names);
                    JSONObject properties = new JSONObject();
                    properties.set("version", "1.0");
                    properties.set("format", "record-stream");
                    properties.set("description", "SolidBase JSON Data Dump File");
                    properties.set("createdBy", new JSONObject("product", "SolidBase", "version", "2.0.0"));
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    properties.set("createdDate", format.format(new Date()));
                    Resource binResource = Resources.getResource(parsed.binaryFileName);
                    Resource resource = Resources.getResource(parsed.fileName);
                    properties.set("binaryFile", binResource.getPathFrom(resource).toString());
                    JSONArray fields = new JSONArray();
                    properties.set("fields", fields);
                    for (int i = 0; i < columns; i++) if (!ignore[i]) {
                        JSONObject field = new JSONObject();
                        field.set("schemaName", schemaNames[i]);
                        field.set("tableName", tableNames[i]);
                        field.set("name", names[i]);
                        field.set("type", JDBCSupport.toTypeName(types[i]));
                        FileSpec spec = fileSpecs[i];
                        if (spec != null && !spec.generator.isDynamic()) {
                            Resource fileResource = new FileResource(spec.generator.fileName);
                            field.set("file", fileResource.getPathFrom(jsvResource).toString());
                        }
                        fields.add(field);
                    }
                    FileSpec binaryFile = parsed.binaryFileName != null ? new FileSpec(true, parsed.binaryFileName, 0) : null;
                    jsonWriter.writeFormatted(properties, 120);
                    jsonWriter.getWriter().write('\n');
                    try {
                        long count = 0;
                        long progressNext = 1;
                        while (result.next()) {
                            Object[] values = new Object[columns];
                            for (int i = 0; i < values.length; i++) values[i] = JDBCSupport.getValue(result, types, i);
                            if (parsed.coalesce != null) parsed.coalesce.coalesce(values);
                            JSONArray array = new JSONArray();
                            for (int i = 0; i < columns; i++) if (!ignore[i]) {
                                Object value = values[i];
                                if (value == null) {
                                    array.add(null);
                                    continue;
                                }
                                FileSpec spec = fileSpecs[i];
                                if (spec != null) {
                                    String relFileName = null;
                                    int startIndex;
                                    if (spec.binary) {
                                        if (spec.generator.isDynamic()) {
                                            String fileName = spec.generator.generateFileName(result);
                                            Resource fileResource = new FileResource(fileName);
                                            spec.out = fileResource.getOutputStream();
                                            spec.index = 0;
                                            relFileName = fileResource.getPathFrom(jsvResource).toString();
                                        } else if (spec.out == null) {
                                            String fileName = spec.generator.generateFileName(result);
                                            Resource fileResource = new FileResource(fileName);
                                            spec.out = fileResource.getOutputStream();
                                        }
                                        if (value instanceof Blob) {
                                            InputStream in = ((Blob) value).getBinaryStream();
                                            startIndex = spec.index;
                                            byte[] buf = new byte[4096];
                                            for (int read = in.read(buf); read >= 0; read = in.read(buf)) {
                                                spec.out.write(buf, 0, read);
                                                spec.index += read;
                                            }
                                            in.close();
                                        } else if (value instanceof byte[]) {
                                            startIndex = spec.index;
                                            spec.out.write((byte[]) value);
                                            spec.index += ((byte[]) value).length;
                                        } else throw new CommandFileException(names[i] + " (" + value.getClass().getName() + ") is not a binary column. Only binary columns like BLOB, RAW, BINARY VARYING can be written to a binary file", command.getLocation());
                                        if (spec.generator.isDynamic()) {
                                            spec.out.close();
                                            JSONObject ref = new JSONObject();
                                            ref.set("file", relFileName);
                                            ref.set("size", spec.index - startIndex);
                                            array.add(ref);
                                        } else {
                                            JSONObject ref = new JSONObject();
                                            ref.set("index", startIndex);
                                            ref.set("length", spec.index - startIndex);
                                            array.add(ref);
                                        }
                                    } else {
                                        if (spec.generator.isDynamic()) {
                                            String fileName = spec.generator.generateFileName(result);
                                            Resource fileResource = new FileResource(fileName);
                                            spec.writer = new DeferringWriter(spec.threshold, fileResource, jsonWriter.getEncoding());
                                            spec.index = 0;
                                            relFileName = fileResource.getPathFrom(jsvResource).toString();
                                        } else if (spec.writer == null) {
                                            String fileName = spec.generator.generateFileName(result);
                                            Resource fileResource = new FileResource(fileName);
                                            spec.writer = new OutputStreamWriter(fileResource.getOutputStream(), jsonWriter.getEncoding());
                                        }
                                        if (value instanceof Blob || value instanceof byte[]) throw new CommandFileException(names[i] + " is a binary column. Binary columns like BLOB, RAW, BINARY VARYING cannot be written to a text file", command.getLocation());
                                        if (value instanceof Clob) {
                                            Reader in = ((Clob) value).getCharacterStream();
                                            startIndex = spec.index;
                                            char[] buf = new char[4096];
                                            for (int read = in.read(buf); read >= 0; read = in.read(buf)) {
                                                spec.writer.write(buf, 0, read);
                                                spec.index += read;
                                            }
                                            in.close();
                                        } else {
                                            String val = value.toString();
                                            startIndex = spec.index;
                                            spec.writer.write(val);
                                            spec.index += val.length();
                                        }
                                        if (spec.generator.isDynamic()) {
                                            DeferringWriter writer = (DeferringWriter) spec.writer;
                                            if (writer.isBuffered()) array.add(writer.clearBuffer()); else {
                                                JSONObject ref = new JSONObject();
                                                ref.set("file", relFileName);
                                                ref.set("size", spec.index - startIndex);
                                                array.add(ref);
                                            }
                                            writer.close();
                                        } else {
                                            JSONObject ref = new JSONObject();
                                            ref.set("index", startIndex);
                                            ref.set("length", spec.index - startIndex);
                                            array.add(ref);
                                        }
                                    }
                                } else if (value instanceof Clob) array.add(((Clob) value).getCharacterStream()); else if (binaryFile != null && (value instanceof Blob || value instanceof byte[])) {
                                    if (binaryFile.out == null) {
                                        String fileName = binaryFile.generator.generateFileName(null);
                                        Resource fileResource = new FileResource(fileName);
                                        binaryFile.out = fileResource.getOutputStream();
                                    }
                                    int startIndex = binaryFile.index;
                                    if (value instanceof Blob) {
                                        InputStream in = ((Blob) value).getBinaryStream();
                                        byte[] buf = new byte[4096];
                                        for (int read = in.read(buf); read >= 0; read = in.read(buf)) {
                                            binaryFile.out.write(buf, 0, read);
                                            binaryFile.index += read;
                                        }
                                        in.close();
                                    } else {
                                        binaryFile.out.write((byte[]) value);
                                        binaryFile.index += ((byte[]) value).length;
                                    }
                                    JSONObject ref = new JSONObject();
                                    ref.set("index", startIndex);
                                    ref.set("length", binaryFile.index - startIndex);
                                    array.add(ref);
                                } else array.add(value);
                            }
                            for (ListIterator<Object> i = array.iterator(); i.hasNext(); ) {
                                Object value = i.next();
                                if (value instanceof java.sql.Date || value instanceof java.sql.Time || value instanceof java.sql.Timestamp || value instanceof java.sql.RowId) i.set(value.toString());
                            }
                            jsonWriter.write(array);
                            jsonWriter.getWriter().write('\n');
                            count++;
                            if (count >= progressNext) {
                                progressNext = count + count / 10;
                                processor.getProgressListener().println("Written " + count + " records...");
                            }
                        }
                        processor.getProgressListener().println("Written " + count + " records.");
                    } finally {
                        for (FileSpec fileSpec : fileSpecs) if (fileSpec != null) {
                            if (fileSpec.out != null) fileSpec.out.close();
                            if (fileSpec.writer != null) fileSpec.writer.close();
                        }
                        if (binaryFile != null && binaryFile.out != null) binaryFile.out.close();
                    }
                } finally {
                    processor.closeStatement(statement, true);
                }
            } finally {
                jsonWriter.close();
            }
        } catch (IOException e) {
            throw new SystemException(e);
        }
        return true;
    }
