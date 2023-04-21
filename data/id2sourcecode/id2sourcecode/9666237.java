    int[] writeTableToDataFile(Table table, DatabaseFile source, RandomAccessFile dest) throws IOException, SQLException {
        UnifiedTable pointerLookup = new UnifiedTable(int.class, 2, 1000000, 100000);
        int[] rootsArray = table.getIndexRootsArray();
        Index index = table.getPrimaryIndex();
        Node n = index.first();
        RawDiskRow readRow = new RawDiskRow();
        long pos = dest.getFilePointer();
        int count = 0;
        int[] pointerPair = new int[2];
        System.out.println("lookup begins: " + new java.util.Date(System.currentTimeMillis()));
        for (; n != null; count++) {
            CachedRow row = (CachedRow) n.getRow();
            int oldPointer = row.iPos;
            source.readSeek(oldPointer);
            readRow.read(source, rootsArray.length);
            int newPointer = (int) dest.getFilePointer();
            readRow.write(dest);
            pointerPair[0] = oldPointer;
            pointerPair[1] = newPointer;
            pointerLookup.addRow(pointerPair);
            if (count % 50000 == 0) {
                Trace.printSystemOut("pointer pair for row " + oldPointer + " " + newPointer);
            }
            n = index.next(n);
        }
        Trace.printSystemOut(table.getName().name + " transfered");
        dest.seek(pos);
        System.out.println("sort begins: " + new java.util.Date(System.currentTimeMillis()));
        pointerLookup.sort(0, true);
        System.out.println("sort ends: " + new java.util.Date(System.currentTimeMillis()));
        for (int i = 0; i < count; i++) {
            readRow.readNodes(dest, rootsArray.length);
            readRow.replacePointers(pointerLookup);
            dest.seek(readRow.filePosition);
            readRow.writeNodes(dest);
            dest.seek(readRow.filePosition + readRow.storageSize);
            if (i != 0 && i % 50000 == 0) {
                System.out.println(i + " rows " + new java.util.Date(System.currentTimeMillis()));
            }
        }
        for (int i = 0; i < rootsArray.length; i++) {
            int lookupIndex = pointerLookup.search(rootsArray[i]);
            if (lookupIndex == -1) {
                throw new SQLException();
            }
            rootsArray[i] = pointerLookup.getIntCell(lookupIndex, 1);
        }
        Trace.printSystemOut(table.getName().name + " : table converted");
        return rootsArray;
    }
