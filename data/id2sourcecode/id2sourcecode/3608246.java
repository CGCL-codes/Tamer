    public static void saveSamplePackage(SamplePackage pkg, File pkgFile, ProgressCallback prog) throws PackageGenerationException {
        ZipOutputStream os = null;
        ObjectOutputStream oos = null;
        try {
            os = new ZipOutputStream(new FileOutputStream(pkgFile));
            os.setMethod(ZipOutputStream.DEFLATED);
            os.setLevel(Deflater.BEST_SPEED);
            os.putNextEntry(new ZipEntry(SamplePackage.SAMPLE_PKG_CONTENT_ENTRY));
            oos = new ObjectOutputStream(os);
            File smplDir = ZUtilities.replaceExtension(pkgFile, SamplePackage.SAMPLE_DIR_EXT);
            writeSamplePackage(pkg, oos, smplDir, prog);
            oos.close();
        } catch (IOException e) {
            pkgFile.delete();
            throw new PackageGenerationException(e.getMessage());
        }
    }
