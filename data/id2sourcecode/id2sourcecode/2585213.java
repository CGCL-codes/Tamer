    @Test
    public void testDefectiveEntries() throws Exception {
        File logFile = new File(testdir + "1.1.dbl");
        int[] offsets = new int[100];
        final AtomicInteger count = new AtomicInteger(0);
        int totalSize = 0;
        SyncListener sl = new SyncListener() {

            public void synced(LSN lsn) {
                synchronized (count) {
                    count.incrementAndGet();
                    count.notifyAll();
                }
            }

            public void failed(Exception ex) {
                fail("this should not happen");
                synchronized (count) {
                    count.incrementAndGet();
                    count.notifyAll();
                }
            }
        };
        for (int i = 0; i < 100; i++) {
            String pl = "Entry " + (i + 1);
            ReusableBuffer plb = ReusableBuffer.wrap(pl.getBytes());
            LogEntry e = new LogEntry(plb, sl, LogEntry.PAYLOAD_TYPE_INSERT);
            int entrySize = LogEntry.headerLength + e.getPayload().remaining();
            if (i < 99) offsets[i + 1] = offsets[i] + entrySize; else totalSize = offsets[offsets.length - 1] + entrySize;
            l.append(e);
        }
        synchronized (count) {
            while (count.get() < 100) count.wait(1000);
        }
        System.out.println("finished writing");
        try {
            l.lock();
            l.switchLogFile(false);
        } finally {
            l.unlock();
        }
        assertEquals(totalSize, logFile.length());
        File tmpFile = new File(testdir + "log.dbl");
        copyFile(logFile, tmpFile);
        RandomAccessFile raf = new RandomAccessFile(tmpFile.getAbsolutePath(), "rw");
        raf.seek(Integer.SIZE / 8);
        raf.writeInt(999999);
        raf.close();
        DiskLogFile f = new DiskLogFile(tmpFile.getAbsolutePath());
        assertFalse(f.hasNext());
        f.close();
        assertTrue(tmpFile.delete());
        copyFile(logFile, tmpFile);
        raf = new RandomAccessFile(tmpFile.getAbsoluteFile(), "rw");
        raf.writeInt(2);
        raf.close();
        f = new DiskLogFile(tmpFile.getAbsolutePath());
        assertFalse(f.hasNext());
        f.close();
        assertTrue(tmpFile.delete());
        copyFile(logFile, tmpFile);
        raf = new RandomAccessFile(tmpFile.getAbsolutePath(), "rw");
        raf.seek(offsets[50]);
        raf.writeInt(79);
        raf.close();
        f = new DiskLogFile(tmpFile.getAbsolutePath());
        for (int i = 0; i < 50; i++) {
            LogEntry next = f.next();
            assertNotNull(next);
            next.free();
        }
        assertFalse(f.hasNext());
        f.close();
        assertTrue(tmpFile.delete());
        copyFile(logFile, tmpFile);
        raf = new RandomAccessFile(tmpFile.getAbsolutePath(), "rw");
        raf.seek(offsets[50]);
        raf.writeInt(-122);
        raf.close();
        f = new DiskLogFile(tmpFile.getAbsolutePath());
        for (int i = 0; i < 50; i++) {
            LogEntry next = f.next();
            assertNotNull(next);
            next.free();
        }
        assertFalse(f.hasNext());
        f.close();
        assertTrue(tmpFile.delete());
        copyFile(logFile, tmpFile);
        raf = new RandomAccessFile(tmpFile.getAbsolutePath(), "rw");
        raf.getChannel().truncate(offsets[99] + 5);
        raf.close();
        f = new DiskLogFile(tmpFile.getAbsolutePath());
        for (int i = 0; i < 99; i++) {
            LogEntry next = f.next();
            assertNotNull(next);
            next.free();
        }
        assertFalse(f.hasNext());
        f.close();
        logFile.delete();
        copyFile(tmpFile, logFile);
        tmpFile.delete();
        l.shutdown();
        l = new DiskLogger(testdir, new LSN(1, 200L), SyncMode.FSYNC, 0, 0);
        l.start();
        for (int i = 99; i < 120; i++) {
            String pl = "Entry " + (i + 1);
            ReusableBuffer plb = ReusableBuffer.wrap(pl.getBytes());
            l.append(new LogEntry(plb, sl, LogEntry.PAYLOAD_TYPE_INSERT));
        }
        synchronized (count) {
            if (count.get() < 121) count.wait(1000);
        }
        File[] logFiles = new File(testdir).listFiles();
        DiskLogIterator it = new DiskLogIterator(logFiles, null);
        for (int i = 0; i < 120; i++) {
            LogEntry next = it.next();
            assertNotNull(next);
            next.free();
        }
        assertFalse(it.hasNext());
    }
