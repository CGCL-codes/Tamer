    public void repackageNonJavaFile(String name) throws IOException {
        File sourceFile = new File(_sourceBase, name);
        File targetFile = new File(_targetBase, name);
        if (sourceFile.lastModified() < targetFile.lastModified()) _skippedFiles += 1; else writeFile(targetFile, _repackager.repackage(readFile(sourceFile)));
    }
