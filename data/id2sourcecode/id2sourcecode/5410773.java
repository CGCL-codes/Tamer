    public QDataSet getDataSet(ProgressMonitor mon) throws Exception {
        File f = getFile(mon);
        FileChannel fc = new FileInputStream(f).getChannel();
        final int offset = getIntParameter("byteOffset", 0);
        int defLen = (int) f.length() - offset;
        int length = getIntParameter("byteLength", defLen);
        if (length == defLen && (f.length() - (long) offset) > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("default length is too big!");
        }
        int fieldCount = getIntParameter("fieldCount", params.get("depend0") == null ? 1 : 2);
        int recCount = getIntParameter("recCount", Integer.MAX_VALUE);
        ByteBuffer buf = fc.map(MapMode.READ_ONLY, offset, length);
        fc.close();
        String recFormat = getParameter("recFormat", null);
        Object[] recFormatParse = null;
        if (recFormat != null) {
            recFormatParse = parseRecFormat(recFormat);
            fieldCount = (Integer) recFormatParse[2];
        }
        int dep0 = getIntParameter("depend0", -1);
        int dep0Offset = getIntParameter("depend0Offset", -1);
        int defltcol;
        if ((dep0 == -1) && (dep0Offset == -1)) {
            defltcol = 0;
        } else {
            if (dep0 > 0 || dep0Offset > 0) {
                defltcol = 0;
            } else {
                defltcol = 1;
            }
        }
        int col = getIntParameter("column", defltcol);
        String columnType;
        String colType = String.valueOf(BufferDataSet.UBYTE);
        if (recFormatParse != null) {
            String o = params.get("rank2");
            if (o != null) {
                throw new IllegalArgumentException("rank2 and columnFormat are not supported");
            } else {
                Object[] types = ((Object[]) recFormatParse[1]);
                colType = String.valueOf(types[col]);
            }
        }
        columnType = getParameter("type", colType);
        int recSizeBytes = getIntParameter("recLength", -1);
        if (recFormatParse != null) {
            recSizeBytes = (Integer) recFormatParse[3];
        }
        if (recSizeBytes == -1) recSizeBytes = BufferDataSet.byteCount(columnType) * fieldCount;
        if (recFormatParse == null) {
            fieldCount = recSizeBytes / BufferDataSet.byteCount(columnType);
        }
        final int frecCount = Math.min(length / recSizeBytes, recCount);
        int[] rank2 = null;
        String o = params.get("rank2");
        if (o != null) {
            String s = o;
            int first = 0;
            int last = -999;
            if (s.contains(":")) {
                String[] ss = s.split(":", -2);
                if (ss[0].length() > 0) {
                    first = Integer.parseInt(ss[0]);
                }
                if (ss.length > 1 && ss[1].length() > 0) {
                    last = Integer.parseInt(ss[1]);
                }
            }
            if (last == -999) last = fieldCount;
            rank2 = new int[] { first, last };
            col = first;
            if (col < 0) col = fieldCount + col;
            if (first > fieldCount) throw new IndexOutOfBoundsException("rank 2 index is greater than field count");
            if (last > fieldCount) throw new IndexOutOfBoundsException("rank 2 index is greater than field count");
        }
        int recOffset = getIntParameter("recOffset", -1);
        if (recOffset == -1) {
            if (recFormatParse != null) {
                recOffset = ((int[]) recFormatParse[0])[col];
            } else {
                recOffset = col * BufferDataSet.byteCount(columnType);
            }
        }
        String encoding = getParameter("byteOrder", "little");
        if (encoding.equals("big")) {
            buf.order(ByteOrder.BIG_ENDIAN);
        } else {
            buf.order(ByteOrder.LITTLE_ENDIAN);
        }
        MutablePropertyDataSet ds;
        if (rank2 != null) {
            if (rank2[1] == -999) {
                rank2[1] = frecCount;
            }
            if (rank2[1] < 0) {
                rank2[1] = fieldCount + rank2[1];
            }
            if (rank2[0] < 0) {
                rank2[0] = fieldCount + rank2[0];
            }
            ds = BufferDataSet.makeDataSet(2, recSizeBytes, recOffset, frecCount, rank2[1] - rank2[0], 1, 1, buf, columnType);
        } else {
            ds = BufferDataSet.makeDataSet(1, recSizeBytes, recOffset, frecCount, 1, 1, 1, buf, columnType);
        }
        if (dep0 > -1 || dep0Offset > -1) {
            String dep0Type = getParameter("depend0Type", columnType);
            if (recFormatParse != null) {
                dep0Type = getParameter("depend0Type", String.valueOf(((Object[]) recFormatParse[1])[dep0]));
            }
            if (dep0Offset == -1) {
                if (recFormatParse == null) {
                    dep0Offset = BufferDataSet.byteCount(dep0Type) * dep0;
                } else {
                    dep0Offset = ((int[]) recFormatParse[0])[dep0];
                }
            }
            BufferDataSet dep0ds = BufferDataSet.makeDataSet(1, recSizeBytes, dep0Offset, frecCount, 1, 1, 1, buf, dep0Type);
            String dep0Units = getParameter("depend0Units", "");
            if (dep0Units.length() > 0) {
                dep0Units = dep0Units.replaceAll("\\+", " ");
                Units dep0u = SemanticOps.lookupUnits(dep0Units);
                dep0ds.putProperty(QDataSet.UNITS, dep0u);
            }
            ds.putProperty(QDataSet.DEPEND_0, dep0ds);
        } else {
            boolean reportOffset = !(getParameter("reportOffset", "no").equals("no"));
            if (reportOffset) {
                final int finalRecSizeBytes = recSizeBytes;
                final int finalRecOffset = recOffset;
                IndexGenDataSet dep0ds = new IndexGenDataSet(frecCount) {

                    @Override
                    public double value(int i) {
                        return offset + finalRecOffset + i * finalRecSizeBytes;
                    }
                };
                dep0ds.putProperty(QDataSet.CADENCE, DataSetUtil.asDataSet((double) recSizeBytes));
                ds.putProperty(QDataSet.DEPEND_0, dep0ds);
            }
        }
        String s;
        s = params.get("validMin");
        if (s != null) {
            ds.putProperty(QDataSet.VALID_MIN, java.lang.Double.parseDouble(s));
        }
        s = params.get("validMax");
        if (s != null) {
            ds.putProperty(QDataSet.VALID_MAX, java.lang.Double.parseDouble(s));
        }
        return ds;
    }
