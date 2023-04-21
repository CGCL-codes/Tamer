    public final void testWriteBitmap() throws IOException {
        (new File("images/test/")).mkdir();
        BitmapObject bmo = BitmapFile.readBitmap("images/in_write.bmp");
        BitmapFile.writeBitmap("images/test/test_write.bmp", bmo);
        assertEquals(true, compareFiles("images/test/test_write.bmp", "images/in_write.bmp"));
    }
