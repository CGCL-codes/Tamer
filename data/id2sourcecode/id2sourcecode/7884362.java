    @TestTargetNew(level = TestLevel.SUFFICIENT, notes = "Tests against golden file missing.", method = "writeChars", args = { java.lang.String.class })
    public void test_writeCharsLjava_lang_String() throws IOException {
        RandomAccessFile raf = new java.io.RandomAccessFile(fileName, "rw");
        raf.writeChars(unihw);
        char[] hchars = new char[unihw.length()];
        unihw.getChars(0, unihw.length(), hchars, 0);
        raf.seek(0);
        for (int i = 0; i < hchars.length; i++) assertEquals("Test 1: Incorrect character written or read at index " + i + ";", hchars[i], raf.readChar());
        raf.close();
        try {
            raf.writeChars("Already closed.");
            fail("Test 2: IOException expected.");
        } catch (IOException e) {
        }
    }
