    private static void unzip(File destDir, File zipFile) throws Exception {
        ZipInputStream zipIn = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
        for (ZipEntry e = zipIn.getNextEntry(); e != null; e = zipIn.getNextEntry()) {
            File outFile = new File(destDir, e.getName());
            FileUtils.copyFile(zipIn, outFile);
            outFile.setLastModified(e.getTime());
        }
    }
