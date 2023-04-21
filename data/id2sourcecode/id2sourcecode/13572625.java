    @Test
    public void testCopyStream02Buffer() throws IOException {
        URL url = IOUtil.getResource(this.getClass(), tfilename);
        Assert.assertNotNull(url);
        final ByteBuffer bb = IOUtil.copyStream2ByteBuffer(new BufferedInputStream(url.openStream()));
        Assert.assertEquals("Byte number not equal orig vs buffer", orig.length, bb.limit());
        int i;
        for (i = tsz - 1; i >= 0 && orig[i] == bb.get(i); i--) ;
        Assert.assertTrue("Bytes not equal orig vs array", 0 > i);
    }
