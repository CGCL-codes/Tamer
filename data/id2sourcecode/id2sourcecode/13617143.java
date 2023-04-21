    public void copy(File pSourceFile, File pTargetFile) throws CisCoreException {
        try {
            FileUtils.copyFile(pSourceFile, pTargetFile);
        } catch (IOException e) {
            throw new CisCoreException("Copying source file " + pSourceFile.getPath() + " to target file " + pTargetFile.getPath() + " failed: " + e.getMessage(), e);
        }
    }
