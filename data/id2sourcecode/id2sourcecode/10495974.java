    private static ParadoxIndex loadIndexHeader(final ParadoxConnection conn, final File file) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(2048);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        FileChannel channel = null;
        final FileInputStream fs = new FileInputStream(file);
        final ParadoxIndex index = new ParadoxIndex(file, file.getName());
        try {
            channel = fs.getChannel();
            channel.read(buffer);
            buffer.flip();
            index.setRecordSize(buffer.getShort());
            index.setHeaderSize(buffer.getShort());
            index.setType(buffer.get());
            index.setBlockSize(buffer.get());
            index.setRowCount(buffer.getInt());
            index.setUsedBlocks(buffer.getShort());
            index.setTotalBlocks(buffer.getShort());
            index.setFirstBlock(buffer.getShort());
            index.setLastBlock(buffer.getShort());
            buffer.position(0x21);
            index.setFieldCount(buffer.getShort());
            index.setPrimaryFieldCount(buffer.getShort());
            buffer.position(0x38);
            index.setWriteProtected(buffer.get());
            index.setVersionId(buffer.get());
            buffer.position(0x49);
            index.setAutoIncrementValue(buffer.getInt());
            index.setFirstFreeBlock(buffer.getShort());
            buffer.position(0x55);
            index.setReferencialIntegrity(buffer.get());
            if (index.getVersionId() > 4) {
                buffer.position(0x6A);
                index.setCharset(Charset.forName("cp" + buffer.getShort()));
                buffer.position(0x78);
            } else {
                buffer.position(0x58);
            }
            final ArrayList<ParadoxField> fields = new ArrayList<ParadoxField>();
            for (int loop = 0; loop < index.getFieldCount(); loop++) {
                final ParadoxField field = new ParadoxField();
                field.setType(buffer.get());
                field.setSize(buffer.get());
                fields.add(field);
            }
            if (index.getVersionId() > 4) {
                if (index.getVersionId() == 0xC) {
                    buffer.position(0x78 + 261 + 4 + 6 * fields.size());
                } else {
                    buffer.position(0x78 + 83 + 6 * fields.size());
                }
            } else {
                buffer.position(0x58 + 83 + 6 * fields.size());
            }
            for (int loop = 0; loop < index.getFieldCount(); loop++) {
                final ByteBuffer name = ByteBuffer.allocate(261);
                while (true) {
                    final byte c = buffer.get();
                    if (c == 0) {
                        break;
                    }
                    name.put(c);
                }
                name.flip();
                fields.get(loop).setName(index.getCharset().decode(name).toString());
            }
            index.setFields(fields);
            final ArrayList<Short> fieldsOrder = new ArrayList<Short>();
            for (int loop = 0; loop < index.getFieldCount(); loop++) {
                fieldsOrder.add(buffer.getShort());
            }
            index.setFieldsOrder(fieldsOrder);
            final ByteBuffer sortOrderID = ByteBuffer.allocate(26);
            while (true) {
                final byte c = buffer.get();
                if (c == 0) {
                    break;
                }
                sortOrderID.put(c);
            }
            sortOrderID.flip();
            index.setSortOrderID(index.getCharset().decode(sortOrderID).toString());
            final ByteBuffer name = ByteBuffer.allocate(26);
            while (true) {
                final byte c = buffer.get();
                if (c == 0) {
                    break;
                }
                name.put(c);
            }
            name.flip();
            String tempName = index.getCharset().decode(name).toString();
            if (tempName.length() != 0) {
                index.setName(tempName);
            }
        } finally {
            if (channel != null) {
                channel.close();
            }
            fs.close();
        }
        return index;
    }
