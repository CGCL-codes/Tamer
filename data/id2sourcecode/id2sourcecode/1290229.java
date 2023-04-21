    private static void checkDetector(final FormatEnum expectedFormat, final String[] extensions) throws Exception {
        URL url = TestDroidDetector.class.getResource("/testFiles");
        final String path = url.getPath();
        Iterator<File> fiter = FileUtils.iterateFiles(new File(path), extensions, false);
        assertTrue("at least one file", fiter.hasNext());
        while (fiter.hasNext()) {
            File file = fiter.next();
            final InputStream is = new FileInputStream(file);
            String fileName = file.getName();
            final GuessInputStream gis = GuessInputStream.getInstance(is);
            assertEquals("file format [" + fileName + "]", expectedFormat, gis.getFormat());
            final byte[] reference = IOUtils.toByteArray(new FileInputStream(file));
            assertTrue("Read equals reference [" + fileName + "]", Arrays.equals(reference, IOUtils.toByteArray(gis)));
            gis.close();
        }
        final String[] badFiles = TestUtils.listFilesExcludingExtension(extensions);
        for (int i = 0; i < badFiles.length; i++) {
            final String fileName = badFiles[i];
            final GuessInputStream gis = GuessInputStream.getInstance(new FileInputStream(fileName));
            assertTrue("file [" + fileName + "] WAS UNCORRECTLY recognized as [" + expectedFormat + "]", !expectedFormat.equals(gis.getFormat()));
            gis.close();
        }
    }
