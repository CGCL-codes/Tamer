    public void test_writeI() throws IOException {
        cw.write('T');
        cr = new CharArrayReader(cw.toCharArray());
        assertEquals("Writer failed to write char", 'T', cr.read());
    }
