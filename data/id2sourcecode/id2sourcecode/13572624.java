    @Test
    public void testCopyStream01Array() throws IOException {
        URL url = IOUtil.getResource(this.getClass(), tfilename);
        Assert.assertNotNull(url);
        final byte[] bb = IOUtil.copyStream2ByteArray(new BufferedInputStream(url.openStream()));
        Assert.assertEquals("Byte number not equal orig vs array", orig.length, bb.length);
        Assert.assertTrue("Bytes not equal orig vs array", Arrays.equals(orig, bb));
    }
