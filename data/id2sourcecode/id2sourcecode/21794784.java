    private File createTestHierarchy(File source) throws IOException {
        File testDir = new File(System.getProperty("java.io.tmpdir"), "mstor_test" + File.separator + super.getName() + File.separator + source.getName());
        FileUtils.copyFileToDirectory(source, testDir);
        return new File(testDir, source.getName());
    }
