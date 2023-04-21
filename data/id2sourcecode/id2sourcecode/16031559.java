    public void testInsertEntryChannel() throws IOException {
        File entryFile = helper.getTestCaseFile(this);
        {
            FileWriter writer = new FileWriter(entryFile);
            String[] junk = new String[] { "cows jumping over the moon ", "quick foxes and lazy dogs ", "cdos, cmos, sivs, and cdss ", "print code instead of money ", "this time it's different ", "the next one will be even better " };
            Random rnd = new Random(0);
            for (int count = 500; count-- > 0; ) {
                int i = rnd.nextInt(junk.length);
                writer.write(junk[i]);
                if (count % 3 == 0) writer.write('\n');
            }
            writer.close();
        }
        File segmentRootDir = new File(ROOT_DIR, getName());
        SegmentStore store = SegmentStore.writeNewInstance(segmentRootDir.getPath());
        Segment readOnlySegment = store.getReadOnlySegment();
        TxnSegment txn = store.newTransaction();
        FileChannel entry = new FileInputStream(entryFile).getChannel();
        txn.insertEntry(entry);
        txn.commit();
        FileChannel insertedEntry = readOnlySegment.getEntryChannel(0);
        entry.position(0);
        long count = entry.size();
        ByteBuffer ob = ByteBuffer.allocate(1);
        ByteBuffer ib = ByteBuffer.allocate(1);
        while (count-- > 0) {
            ob.clear();
            ib.clear();
            entry.read(ob);
            insertedEntry.read(ib);
            assertEquals(ob.get(0), ib.get(0));
        }
        entry.close();
        store.close();
    }
