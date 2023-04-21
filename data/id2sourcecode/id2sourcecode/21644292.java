    public static ArrayList<ArrayList<AbstractFieldValue>> loadData(final ParadoxConnection conn, final ParadoxTable table, final Collection<ParadoxField> fields) throws IOException, SQLException {
        final ArrayList<ArrayList<AbstractFieldValue>> ret = new ArrayList<ArrayList<AbstractFieldValue>>();
        final FileInputStream fs = new FileInputStream(table.getFile());
        final int blockSize = table.getBlockSizeBytes();
        final int recordSize = table.getRecordSize();
        final int headerSize = table.getHeaderSize();
        final ByteBuffer buffer = ByteBuffer.allocate(blockSize);
        FileChannel channel = null;
        try {
            channel = fs.getChannel();
            if (table.getUsedBlocks() > 0) {
                int nextBlock = 1;
                do {
                    buffer.order(ByteOrder.LITTLE_ENDIAN);
                    channel.position(headerSize + ((nextBlock - 1) * blockSize));
                    buffer.clear();
                    channel.read(buffer);
                    buffer.flip();
                    nextBlock = buffer.getShort();
                    buffer.getShort();
                    final int addDataSize = buffer.getShort();
                    final int rowsInBlock = (addDataSize / recordSize) + 1;
                    buffer.order(ByteOrder.BIG_ENDIAN);
                    for (int loop = 0; loop < rowsInBlock; loop++) {
                        final ArrayList<AbstractFieldValue> row = new ArrayList<AbstractFieldValue>();
                        for (final ParadoxField field : table.getFields()) {
                            AbstractFieldValue fieldValue = null;
                            switch(field.getType()) {
                                case 1:
                                    {
                                        final ByteBuffer value = ByteBuffer.allocate(field.getSize());
                                        for (int chars = 0; chars < field.getSize(); chars++) {
                                            value.put(buffer.get());
                                        }
                                        value.flip();
                                        final String v = table.getCharset().decode(value).toString();
                                        fieldValue = new StringValue(v);
                                        break;
                                    }
                                case 2:
                                    {
                                        int a1 = (0x000000FF & ((int) buffer.get()));
                                        int a2 = (0x000000FF & ((int) buffer.get()));
                                        int a3 = (0x000000FF & ((int) buffer.get()));
                                        int a4 = (0x000000FF & ((int) buffer.get()));
                                        long days = ((long) (a1 << 24 | a2 << 16 | a3 << 8 | a4)) & 0x0FFFFFFFL;
                                        if ((a1 & 0xB0) != 0) {
                                            final Date date = DateUtils.SdnToGregorian(days + 1721425);
                                            fieldValue = new DateValue(date);
                                        } else {
                                            fieldValue = new DateValue(null);
                                        }
                                        break;
                                    }
                                case 3:
                                    {
                                        final int v = buffer.getShort();
                                        fieldValue = new IntegerValue(v);
                                        break;
                                    }
                                case 5:
                                case 6:
                                    {
                                        final double v = buffer.getDouble() * -1;
                                        if (Double.compare(Double.NEGATIVE_INFINITY, 1 / v) == 0) {
                                            fieldValue = new DoubleValue(null);
                                        } else {
                                            fieldValue = new DoubleValue(v);
                                        }
                                        break;
                                    }
                                case 9:
                                    {
                                        final byte v = buffer.get();
                                        if (v == 0) {
                                            fieldValue = new BooleanValue(null);
                                        } else if (v == -127) {
                                            fieldValue = new BooleanValue(Boolean.TRUE);
                                        } else if (v == -128) {
                                            fieldValue = new BooleanValue(Boolean.TRUE);
                                        } else {
                                            throw new SQLException("Invalid value " + v + ".");
                                        }
                                        break;
                                    }
                                case 0x14:
                                    {
                                        int a1 = (0x000000FF & ((int) buffer.get()));
                                        int a2 = (0x000000FF & ((int) buffer.get()));
                                        int a3 = (0x000000FF & ((int) buffer.get()));
                                        int a4 = (0x000000FF & ((int) buffer.get()));
                                        long timeInMillis = ((long) (a1 << 24 | a2 << 16 | a3 << 8 | a4)) & 0x0FFFFFFFL;
                                        if ((a1 & 0xB0) != 0) {
                                            final Calendar calendar = new GregorianCalendar(1, 0, 0);
                                            calendar.add(Calendar.MILLISECOND, (int) timeInMillis);
                                            final Time time = new Time(calendar.getTimeInMillis());
                                            fieldValue = new TimeValue(time);
                                        } else {
                                            fieldValue = new TimeValue(null);
                                        }
                                        break;
                                    }
                                case 0x16:
                                    {
                                        int v = buffer.getInt() & 0x0FFFFFFF;
                                        fieldValue = new IntegerValue(v);
                                        break;
                                    }
                                default:
                                    throw new SQLException("Type " + field.getType() + " not found.");
                            }
                            if (fields.contains(field)) {
                                row.add(fieldValue);
                            }
                        }
                        ret.add(row);
                    }
                } while (nextBlock != 0);
            }
        } finally {
            if (channel != null) {
                channel.close();
            }
            fs.close();
        }
        return ret;
    }
