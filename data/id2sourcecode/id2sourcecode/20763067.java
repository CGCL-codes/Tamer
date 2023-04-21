    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "PipedReader", args = { java.io.PipedWriter.class })
    public void test_ConstructorLjava_io_PipedWriter() throws IOException {
        try {
            preader = new PipedReader(new PipedWriter());
        } catch (Exception e) {
            fail("Test 1: Constructor failed: " + e.getMessage());
        }
        preader.close();
        PipedWriter pw = new PipedWriter(new PipedReader());
        try {
            preader = new PipedReader(pw);
            fail("Test 2: IOException expected because the writer is already connected.");
        } catch (IOException e) {
        }
    }
