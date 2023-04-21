    public void testCompress() throws InterruptedException, IOException {
        File rawFile = getRawFileSmall();
        File zipped = new File(rawFile + ".bz2");
        zipped.deleteOnExit();
        BZip2.main(new String[] { "-z", "-k", "-f", rawFile.getPath() });
        assertTrue(zipped.isFile());
        long modTime = zipped.lastModified();
        Thread.sleep(5000);
        BufferedInputStream expectIn = new BufferedInputStream(new FileInputStream(rawFile));
        FileInputStream fin = Files.openLockedFileInputStream(zipped);
        fin.getFD().sync();
        fin.getChannel().force(true);
        log.info(zipped + ": " + zipped.length());
        BZip2InputStream actualIn = new BZip2InputStream(new BufferedXInputStream(fin));
        try {
            assertStreamContentEquals(expectIn, actualIn);
        } finally {
            expectIn.close();
            actualIn.close();
        }
        Thread.sleep(1000);
        BZip2.main(new String[] { "-zkf", rawFile.getPath() });
        assertTrue(zipped.isFile());
        assertFalse(zipped.lastModified() == modTime);
        modTime = zipped.lastModified();
        BZip2.main(new String[] { "-z", "-k", rawFile.getPath() });
        assertTrue(zipped.isFile());
        assertEquals(modTime, zipped.lastModified());
        BZip2.main(new String[] { "-z", "-f", rawFile.getPath() });
        assertFalse(rawFile.isFile());
        assertTrue(zipped.isFile());
        zipped.delete();
    }
