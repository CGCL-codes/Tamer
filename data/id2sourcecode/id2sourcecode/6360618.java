    public void buildClass(File sourceDir, File binaryDir) throws Exception {
        File source = this.getFilePackage(sourceDir, ".java");
        this.generateSource(source);
        boolean result = this.compileSource(source);
        if (result) {
            File newBinClass = new File(source.getParent(), source.getName().replaceAll("\\.java", "\\.class"));
            File binaryClass = null;
            binaryClass = this.getFilePackage(binaryDir, ".class");
            try {
                FileUtils.copyFile(newBinClass, binaryClass);
                newBinClass.delete();
            } catch (Exception e) {
                LOGGER.error("Exception : ", e);
            }
        }
    }
