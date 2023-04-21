    @Override
    protected void doCopy(File dest) {
        try {
            FileUtils.copyFile(realFile, dest);
        } catch (IOException ex) {
            throw new RuntimeException("Failed doing copy to: " + dest.getAbsolutePath(), ex);
        }
    }
