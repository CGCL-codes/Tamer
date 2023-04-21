    public void testTransferFromToByteChannel() throws Exception {
        byte[] a = generateBytes();
        File f = Files.createTempFile();
        f.deleteOnExit();
        FilePipe p = new FilePipe(f, false, 0, false);
        assertFalse(p.isDeletingFile());
        assertEquals(0, p.getInitialSize());
        assertTrue(p.sink().isOpen());
        assertTrue(p.source().isOpen());
        ReadableByteChannel in = Channels.newChannel(new ByteArrayInputStream(a));
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(a.length);
        WritableByteChannel out = Channels.newChannel(outStream);
        assertEquals(a.length, p.sink().transferFromByteChannel(in, a.length));
        p.sink().close();
        assertEquals(a.length, p.source().transferToByteChannel(out, -1));
        p.source().close();
        f.delete();
        assertFalse(p.source().isOpen());
        assertFalse(p.sink().isOpen());
        assertFalse(p.isFileOpen());
        byte[] b = outStream.toByteArray();
        assertEquals(a.length, b.length);
        assertTrue(Arrays.equals(a, b));
        assertFalse(f.exists());
    }
