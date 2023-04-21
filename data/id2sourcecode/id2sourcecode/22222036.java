    private void dumpData(String fileName, String outputName, int offset) {
        PrintWriter writer = null;
        FileStore store = null;
        try {
            writer = getWriter(outputName, ".sql");
            writer.println("CREATE ALIAS IF NOT EXISTS READ_CLOB FOR \"" + this.getClass().getName() + ".readClob\";");
            writer.println("CREATE ALIAS IF NOT EXISTS READ_BLOB FOR \"" + this.getClass().getName() + ".readBlob\";");
            resetSchema();
            store = FileStore.open(null, fileName, "r");
            long length = store.length();
            int blockSize = DiskFile.BLOCK_SIZE;
            int blocks = (int) (length / blockSize);
            blockCount = 1;
            int pageCount = blocks / DiskFile.BLOCKS_PER_PAGE;
            int[] pageOwners = new int[pageCount + 1];
            for (block = 0; block < blocks; block += blockCount) {
                store.seek(offset + (long) block * blockSize);
                byte[] buff = new byte[blockSize];
                DataPage s = DataPage.create(this, buff);
                try {
                    store.readFully(buff, 0, blockSize);
                } catch (SQLException e) {
                    writer.println("-- ERROR: can not read: " + e);
                    break;
                }
                blockCount = s.readInt();
                setStorage(-1);
                recordLength = -1;
                valueId = -1;
                if (blockCount == 0) {
                    blockCount = 1;
                    continue;
                } else if (blockCount < 0) {
                    writeDataError(writer, "blockCount<0", s.getBytes(), 1);
                    blockCount = 1;
                    continue;
                } else if (((long) blockCount * blockSize) >= Integer.MAX_VALUE / 4 || (blockCount * blockSize) < 0) {
                    writeDataError(writer, "blockCount=" + blockCount, s.getBytes(), 1);
                    blockCount = 1;
                    continue;
                }
                writer.println("-- block " + block + " - " + (block + blockCount - 1));
                try {
                    s.checkCapacity(blockCount * blockSize);
                } catch (OutOfMemoryError e) {
                    writeDataError(writer, "out of memory", s.getBytes(), 1);
                    blockCount = 1;
                    continue;
                }
                if (blockCount > 1) {
                    if ((blockCount * blockSize) < 0) {
                        writeDataError(writer, "wrong blockCount", s.getBytes(), 1);
                        blockCount = 1;
                        continue;
                    }
                    try {
                        store.readFully(s.getBytes(), blockSize, blockCount * blockSize - blockSize);
                    } catch (Throwable e) {
                        writeDataError(writer, "eof", s.getBytes(), 1);
                        blockCount = 1;
                        store = FileStore.open(null, fileName, "r");
                        continue;
                    }
                }
                try {
                    s.check(blockCount * blockSize);
                } catch (SQLException e) {
                    writeDataError(writer, "wrong checksum", s.getBytes(), 1);
                    blockCount = 1;
                    continue;
                }
                setStorage(s.readInt());
                if (storageId < 0) {
                    writeDataError(writer, "storageId<0", s.getBytes(), blockCount);
                    continue;
                }
                int page = block / DiskFile.BLOCKS_PER_PAGE;
                if (pageOwners[page] != 0 && pageOwners[page] != storageId) {
                    writeDataError(writer, "double allocation, previous=" + pageOwners[page] + " now=" + storageId, s.getBytes(), blockCount);
                } else {
                    pageOwners[page] = storageId;
                }
                Value[] data = createRecord(writer, s);
                if (data != null) {
                    createTemporaryTable(writer);
                    writeRow(writer, s, data);
                }
            }
            writeSchema(writer);
            writer.close();
        } catch (Throwable e) {
            writeError(writer, e);
        } finally {
            IOUtils.closeSilently(writer);
            closeSilently(store);
        }
    }
