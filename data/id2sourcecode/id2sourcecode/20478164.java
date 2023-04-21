    @Test
    public void testWriteReadPage() {
        rep.writePage("test write read", "what are you doing?");
        assertEquals(rep.readPage("test write read"), "what are you doing?");
    }
