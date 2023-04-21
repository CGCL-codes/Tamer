    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies getChannel() method.", method = "getChannel", args = {  })
    @AndroidOnly("The position of the FileChannel for a file opened in " + "append mode is 0 for the RI and corresponds to the " + "next position to write to on Android.")
    public void test_getChannel() throws Exception {
        if (tmpDir == null) {
            throw new Exception("System property java.io.tmpdir not defined.");
        }
        File tmpfile = File.createTempFile("FileOutputStream", "tmp");
        tmpfile.deleteOnExit();
        FileOutputStream fos = new FileOutputStream(tmpfile);
        byte[] b = new byte[10];
        for (int i = 10; i < b.length; i++) {
            b[i] = (byte) i;
        }
        fos.write(b);
        fos.flush();
        fos.close();
        FileOutputStream f = new FileOutputStream(tmpfile, true);
        assertEquals(10, f.getChannel().position());
    }
