    public DataSet read(InputStream input) throws Exception {
        if (input == null) {
            throw new IllegalArgumentException("input == null");
        }
        Map<String, byte[]> mapUnpack = new HashMap<String, byte[]>();
        ZipInputStream zi = new ZipInputStream(input);
        while (true) {
            ZipEntry ze = zi.getNextEntry();
            if (ze == null) break;
            byte[] data = null;
            byte[] buff = new byte[1024];
            ByteArrayOutputStream memOut = new ByteArrayOutputStream();
            while (true) {
                int readed = zi.read(buff);
                if (readed < 0) break;
                memOut.write(buff, 0, readed);
            }
            data = memOut.toByteArray();
            mapUnpack.put(ze.getName(), data);
            zi.closeEntry();
        }
        if (!mapUnpack.containsKey(metaFileName())) return null;
        byte[] bsMeta = mapUnpack.get(metaFileName());
        String sMeta = new String(bsMeta, TextUtil.UTF8());
        if (sMeta == null) return null;
        MetaFile mf = MetaFile.parseXMLString(sMeta);
        if (mf == null) return null;
        if (mf.getSchemaFile() == null) return null;
        if (!mapUnpack.containsKey(mf.getSchemaFile())) return null;
        String sxmlSchema = new String(mapUnpack.get(mf.getSchemaFile()), TextUtil.UTF8());
        if (sxmlSchema == null) return null;
        Document xmlSchema = XMLUtil.parseXML(sxmlSchema);
        if (xmlSchema == null) return null;
        XMLSchemaStorage xSchema = new XMLSchemaStorage();
        DataSet ds = xSchema.restore(xmlSchema);
        if (ds == null) return null;
        Map<String, String> mapTable2File = mf.getMapTable2File();
        if (mapTable2File == null) return ds;
        XMLDataReader xReader = new XMLDataReader();
        for (Table table : ds.getTables()) {
            String tableName = table.getTableName();
            String tableFile = null;
            if (!mapTable2File.containsKey(tableName)) continue;
            tableFile = mapTable2File.get(tableName);
            if (tableFile == null) continue;
            if (!mapUnpack.containsKey(tableFile)) continue;
            byte[] tableDataBytes = mapUnpack.get(tableFile);
            String tableDataText = new String(tableDataBytes, TextUtil.UTF8());
            if (tableDataText == null) continue;
            xReader.readTable(table, tableDataText);
        }
        return ds;
    }
