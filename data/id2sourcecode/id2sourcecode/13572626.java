    @Test
    public void testCopyStream03Buffer() throws IOException {
        final String tfilename2 = "./test2.bin";
        URL url1 = IOUtil.getResource(this.getClass(), tfilename);
        Assert.assertNotNull(url1);
        File file2 = new File(tfilename2);
        IOUtil.copyURL2File(url1, file2);
        URL url2 = IOUtil.getResource(this.getClass(), tfilename2);
        Assert.assertNotNull(url2);
        final ByteBuffer bb = IOUtil.copyStream2ByteBuffer(new BufferedInputStream(url2.openStream()));
        Assert.assertEquals("Byte number not equal orig vs buffer", orig.length, bb.limit());
        int i;
        for (i = tsz - 1; i >= 0 && orig[i] == bb.get(i); i--) ;
        Assert.assertTrue("Bytes not equal orig vs array", 0 > i);
    }
