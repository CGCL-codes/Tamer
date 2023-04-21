    public static void copyResourcesFromClasspath(final String classpathDir, final File outputDir) throws IOException {
        final PathMatchingResourcePatternResolver pmrpr = new PathMatchingResourcePatternResolver();
        final File theDirectory = pmrpr.getResource("classpath:" + classpathDir).getFile();
        final File[] contents = theDirectory.listFiles();
        for (int i = 0; i < contents.length; i++) {
            FileUtils.copyFileToDirectory(contents[i], outputDir);
        }
    }
