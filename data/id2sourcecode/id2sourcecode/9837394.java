    public void repackageNonJavaFile(String sourceName, String targetName) throws IOException {
        File sourceFile = new File(_sourceBase, sourceName);
        File targetFile = new File(_targetBase, targetName);
        if (sourceFile.lastModified() < targetFile.lastModified()) _skippedFiles += 1; else writeFile(targetFile, _repackager.repackage(readFile(sourceFile)));
    }
