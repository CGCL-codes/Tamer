    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "", method = "RandomAccessFile", args = { java.lang.String.class, java.lang.String.class })
    public void test_ConstructorLjava_lang_StringLjava_lang_String() throws IOException {
        RandomAccessFile raf = null;
        File tmpFile = new File(fileName);
        try {
            raf = new java.io.RandomAccessFile(fileName, "r");
            fail("Test 1: FileNotFoundException expected.");
        } catch (FileNotFoundException e) {
        } catch (IllegalArgumentException e) {
            fail("Test 2: Unexpected IllegalArgumentException: " + e.getMessage());
        }
        try {
            try {
                raf = new java.io.RandomAccessFile(fileName, "rwd");
            } catch (IllegalArgumentException e) {
                fail("Test 3: Unexpected IllegalArgumentException: " + e.getMessage());
            }
            raf.close();
            try {
                raf = new java.io.RandomAccessFile(fileName, "rws");
            } catch (IllegalArgumentException e) {
                fail("Test 4: Unexpected IllegalArgumentException: " + e.getMessage());
            }
            raf.close();
            try {
                raf = new java.io.RandomAccessFile(fileName, "rw");
            } catch (IllegalArgumentException e) {
                fail("Test 5: Unexpected IllegalArgumentException: " + e.getMessage());
            }
            raf.close();
            try {
                raf = new java.io.RandomAccessFile(fileName, "i");
                fail("Test 6: IllegalArgumentException expected.");
            } catch (IllegalArgumentException e) {
            }
            raf = new java.io.RandomAccessFile(fileName, "r");
            FileChannel fcr = raf.getChannel();
            try {
                fcr.lock(0L, Long.MAX_VALUE, false);
                fail("Test 7: NonWritableChannelException expected.");
            } catch (NonWritableChannelException e) {
            }
        } finally {
            if (raf != null) raf.close();
            if (tmpFile.exists()) tmpFile.delete();
        }
    }
