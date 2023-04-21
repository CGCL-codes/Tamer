    public File packageDocuments(final String fileName, final DocumentSet<T> documentSet, final File outDir, final String topDir) throws IOException {
        final File dir = new File(outDir.getAbsolutePath() + File.separator + fileName + "_pkg");
        dir.mkdirs();
        File zipStruct;
        if (topDir == null) {
            zipStruct = dir;
        } else {
            zipStruct = new File(dir.getAbsolutePath() + File.separator + topDir);
            zipStruct.mkdirs();
        }
        for (DocumentComponent element : documentSet.getType().getComponents()) {
            for (File file : documentSet.getFiles(element)) {
                FileUtils.copyFileToDirectory(file, documentSet.getFileBasePath(), zipStruct);
            }
        }
        final File out = ZipUtils.createZip(fileName + ".zip", dir, outDir);
        FileUtils.deleteDirectory(dir);
        return out;
    }
