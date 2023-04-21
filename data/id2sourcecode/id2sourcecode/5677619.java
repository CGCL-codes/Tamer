    private File copyBundle(File sourceBundleFile) throws IOException {
        File bundleFile = new File(bundleDirectory, sourceBundleFile.getName());
        FileUtils.copyFile(sourceBundleFile, bundleFile);
        return bundleFile;
    }
