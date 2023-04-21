    private void buildImages(File outDir, File imgDir) throws IOException {
        String[] files = imgDir.list();
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        for (String file : files) {
            if (!file.startsWith(".svn")) {
                FileUtils.copyFileToDirectory(new File(imgDir, file), outDir);
            }
        }
    }
