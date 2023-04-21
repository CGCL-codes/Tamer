    private static ParadoxTable loadTableHeader(final ParadoxConnection conn, final File file) throws IOException {
        final FileInputStream fs = new FileInputStream(file);
        final ParadoxTable table = new ParadoxTable(file, file.getName());
        final ByteBuffer buffer = ByteBuffer.allocate(2048);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        FileChannel channel = null;
        try {
            channel = fs.getChannel();
            channel.read(buffer);
            buffer.flip();
            table.setRecordSize(buffer.getShort());
            table.setHeaderSize(buffer.getShort());
            table.setType(buffer.get());
            table.setBlockSize(buffer.get());
            table.setRowCount(buffer.getInt());
            table.setUsedBlocks(buffer.getShort());
            table.setTotalBlocks(buffer.getShort());
            table.setFirstBlock(buffer.getShort());
            table.setLastBlock(buffer.getShort());
            buffer.position(0x21);
            table.setFieldCount(buffer.getShort());
            table.setPrimaryFieldCount(buffer.getShort());
            buffer.position(0x38);
            table.setWriteProtected(buffer.get());
            table.setVersionId(buffer.get());
            buffer.position(0x49);
            table.setAutoIncrementValue(buffer.getInt());
            table.setFirstFreeBlock(buffer.getShort());
            buffer.position(0x55);
            table.setReferencialIntegrity(buffer.get());
            if (table.getVersionId() > 4) {
                buffer.position(0x6A);
                table.setCharset(Charset.forName("cp" + buffer.getShort()));
                buffer.position(0x78);
            } else {
                buffer.position(0x58);
            }
            final ArrayList<ParadoxField> fields = new ArrayList<ParadoxField>();
            for (int loop = 0; loop < table.getFieldCount(); loop++) {
                final ParadoxField field = new ParadoxField();
                field.setType(buffer.get());
                field.setSize(buffer.get());
                field.setTableName(table.getName());
                field.setTable(table);
                fields.add(field);
            }
            if (table.getVersionId() > 4) {
                if (table.getVersionId() == 0xC) {
                    buffer.position(0x78 + 261 + 4 + 6 * fields.size());
                } else {
                    buffer.position(0x78 + 83 + 6 * fields.size());
                }
            } else {
                buffer.position(0x58 + 83 + 6 * fields.size());
            }
            for (int loop = 0; loop < table.getFieldCount(); loop++) {
                final ByteBuffer name = ByteBuffer.allocate(261);
                while (true) {
                    final byte c = buffer.get();
                    if (c == 0) {
                        break;
                    }
                    name.put(c);
                }
                name.flip();
                fields.get(loop).setName(table.getCharset().decode(name).toString());
            }
            table.setFields(fields);
            final ArrayList<Short> fieldsOrder = new ArrayList<Short>();
            for (int loop = 0; loop < table.getFieldCount(); loop++) {
                fieldsOrder.add(buffer.getShort());
            }
            table.setFieldsOrder(fieldsOrder);
        } finally {
            if (channel != null) {
                channel.close();
            }
            fs.close();
        }
        return table;
    }
