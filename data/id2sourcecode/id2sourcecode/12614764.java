    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies write(int) method.", method = "write", args = { int.class })
    public void test_writeI() {
        cw.write('T');
        cr = new CharArrayReader(cw.toCharArray());
        try {
            assertEquals("Writer failed to write char", 'T', cr.read());
        } catch (IOException e) {
            fail("Exception during write test : " + e.getMessage());
        }
    }
