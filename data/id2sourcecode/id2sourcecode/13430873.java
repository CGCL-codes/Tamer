    private static ParadoxView loadView(final ParadoxConnection conn, final File file) throws IOException, SQLException {
        final ByteBuffer buffer = ByteBuffer.allocate(8192);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        FileChannel channel = null;
        final FileInputStream fs = new FileInputStream(file);
        final ParadoxView view = new ParadoxView(file, file.getName());
        try {
            channel = fs.getChannel();
            channel.read(buffer);
            buffer.flip();
            final BufferedReader reader = new BufferedReader(new StringReader(charset.decode(buffer).toString()));
            if ("Query".equals(reader.readLine())) {
                String line = reader.readLine();
                line = reader.readLine();
                line = reader.readLine().trim();
                if (line.startsWith("FIELDORDER: ")) {
                    line = line.substring("FIELDORDER: ".length());
                    do {
                        line += reader.readLine().trim();
                    } while (line.endsWith(","));
                    ParadoxTable lastTable = null;
                    final ArrayList<ParadoxField> fields = new ArrayList<ParadoxField>();
                    final String[] cols = line.split("\\,");
                    for (final String col : cols) {
                        final String[] i = col.split("->");
                        final ParadoxField field = new ParadoxField();
                        if (i.length < 2) {
                            if (lastTable == null) {
                                throw new SQLException("Invalid table.");
                            }
                            continue;
                        } else {
                            lastTable = getTable(conn, i[0]);
                            field.setName(i[1].substring(1, i[1].length() - 1));
                        }
                        final ParadoxField originalField = getFieldByName(lastTable, field.getName());
                        field.setType(originalField.getType());
                        field.setSize(originalField.getSize());
                        fields.add(field);
                    }
                    view.setFieldsOrder(fields);
                    line = reader.readLine();
                    line = reader.readLine().trim();
                }
                if (line.startsWith("SORT: ")) {
                    line = line.substring("SORT: ".length());
                    do {
                        line += reader.readLine().trim();
                    } while (line.endsWith(","));
                    ParadoxTable lastTable = null;
                    final ArrayList<ParadoxField> fields = new ArrayList<ParadoxField>();
                    final String[] cols = line.split("\\,");
                    for (final String col : cols) {
                        final String[] i = col.split("->");
                        final ParadoxField field = new ParadoxField();
                        if (i.length < 2) {
                            if (lastTable == null) {
                                throw new SQLException("Invalid table.");
                            }
                            continue;
                        } else {
                            lastTable = getTable(conn, i[0]);
                            field.setName(i[1].substring(1, i[1].length() - 1));
                        }
                        final ParadoxField originalField = getFieldByName(lastTable, field.getName());
                        field.setType(originalField.getType());
                        field.setSize(originalField.getSize());
                        fields.add(field);
                    }
                    view.setFieldsSort(fields);
                    line = reader.readLine();
                    line = reader.readLine().trim();
                }
                final ArrayList<ParadoxField> fields = new ArrayList<ParadoxField>();
                do {
                    final String[] flds = line.split("\\|");
                    final String table = flds[0].trim();
                    for (int loop = 1; loop < flds.length; loop++) {
                        final String name = flds[loop].trim();
                        final ParadoxField field = new ParadoxField();
                        final ParadoxField original = getFieldByName(getTable(conn, table), name);
                        field.setTableName(table);
                        field.setName(name);
                        field.setType(original.getType());
                        field.setSize(original.getSize());
                        fields.add(field);
                    }
                    line = reader.readLine();
                    final String[] types = line.split("\\|");
                    for (int loop = 1; loop < types.length; loop++) {
                        if (types[loop].trim().length() > 0) {
                            final ParadoxField field = fields.get(loop - 1);
                            parseExpression(field, types[loop]);
                        }
                    }
                    line = reader.readLine();
                    line = reader.readLine().trim();
                } while (line != null && !"EndQuery".equals(line));
                view.setPrivateFields(fields);
                view.setValid(true);
            }
        } finally {
            if (channel != null) {
                channel.close();
            }
            fs.close();
        }
        return view;
    }
