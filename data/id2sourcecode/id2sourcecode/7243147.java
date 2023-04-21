    @Test
    public void checkThatUserCanForceFilenames() throws IOException {
        File x = File.createTempFile("tmp", ".foo");
        FileUtils.copyFile(getResourceFile("empty.cml"), x);
        Assert.assertFalse(converter.canHandleInput(x));
        converter.convert(x, File.createTempFile("tmp", ".cml"));
        LOG.debug(converter.getWarnings());
        Assert.assertFalse(converter.getWarnings().isEmpty());
    }
