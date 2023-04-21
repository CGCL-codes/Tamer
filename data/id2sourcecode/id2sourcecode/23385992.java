    protected void writeTables(DataSet set, ZipOutputStream zipout, MetaFile metafile) throws IOException {
        XMLDataWriter xWriter = new XMLDataWriter();
        int index = -1;
        for (Table tbl : set.getTables()) {
            index++;
            String fileName = generateTableFileName(index, tbl);
            ZipEntry zeFile = new ZipEntry(fileName);
            zipout.putNextEntry(zeFile);
            xWriter.writeTable(zipout, "UTF8", tbl);
            zipout.closeEntry();
            metafile.getMapTable2File().put(tbl.getTableName(), fileName);
        }
    }
