    private void copyJar(final String jtreemapJar) throws IOException {
        InputStream stream = null;
        try {
            stream = RepoMapPageMaker.class.getResourceAsStream(WEB_FILE_PATH + jtreemapJar);
            if (stream != null) {
                FileUtils.copyFile(stream, new File(ConfigurationOptions.getOutputDir() + jtreemapJar));
            } else {
                throw new IOException("The stream to " + (WEB_FILE_PATH + jtreemapJar) + " failed, is it copied in the jar?");
            }
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
