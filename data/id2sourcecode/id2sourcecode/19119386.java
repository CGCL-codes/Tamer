    @Test(expected = MarkerException.class)
    public void testFileOpenErrors5() throws Exception {
        try {
            runTest("examples/refsim/file_open", "BuildPathErrors5.txt", 1, 0);
        } catch (ZamiaException e) {
            String msg = e.getMessage();
            if (msg.startsWith("Attempt to write to or flush file \"") && msg.endsWith("\" which is opened only for reading.")) {
                throw new MarkerException();
            }
        } finally {
            new File("examples/refsim/file_open/blabla_read.txt").delete();
        }
    }
