    protected void assertText(File fExpected, File fActual) throws IOException {
        if (saveReference) {
            FileUtils.copyFile(fActual, fExpected);
            System.out.println(StringIO.loadTxt(fActual));
        }
        String expected = StringIO.loadTxt(fExpected);
        String actual = StringIO.loadTxt(fActual);
        Assert.assertEquals("Texts are different", expected, actual);
    }
