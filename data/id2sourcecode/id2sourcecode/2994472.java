    private static void processDir(File dir, ZipOutputStream zos, boolean verboseFlag) throws IOException {
        String path = dir.getPath().replace('\\', '/') + "/";
        ZipEntry zEntry = new ZipEntry(path);
        if (verboseFlag) {
            System.out.println("adding: " + path);
        }
        zos.putNextEntry(zEntry);
        File[] filesInDir = dir.listFiles();
        for (int i = 0; i < filesInDir.length; i++) {
            File curFile = filesInDir[i];
            if (curFile.isDirectory()) {
                processDir(curFile, zos, verboseFlag);
            } else {
                processFile(curFile, zos, verboseFlag);
            }
        }
        zos.closeEntry();
    }
