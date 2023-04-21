    public static String copyToDirectory(File file, File sourceDir, File targetDir) {
        String absPath = file.getAbsolutePath();
        String relPath = absPath.substring(sourceDir.getAbsolutePath().length());
        log.debug("processing file : [" + file.getAbsolutePath() + "]");
        int fNamePos = relPath.lastIndexOf(PATH_SEPARATOR);
        String pkgPath = relPath.substring(0, fNamePos);
        String targetPath = targetDir + PATH_SEPARATOR + pkgPath;
        File fTargetPathDir = new File(targetPath);
        if (!fTargetPathDir.exists()) {
            fTargetPathDir.mkdirs();
        }
        try {
            FileUtils.copyFileToDirectory(file, fTargetPathDir);
        } catch (IOException e1) {
            log.error("I/O exception occured while copying file [" + file.getName() + "] to [" + targetDir + "]");
            e1.printStackTrace();
        }
        return pkgPath;
    }
