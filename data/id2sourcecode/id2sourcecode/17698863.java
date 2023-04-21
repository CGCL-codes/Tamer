    public static void gatherDirectoryFiles(File dir, String destinationDirectory, int prefixLng) throws Exception {
        String fromFile = dir.getPath();
        if (dir.isDirectory()) {
            String toFile = destinationDirectory + (fromFile.substring(prefixLng));
            logger.debug("Dir: " + fromFile);
            logger.debug(" =-> " + toFile);
            if (logger.isDebugEnabled()) {
                logger.debug("gatherDirectoryFiles() - :");
            }
            Utils.createDirectory(toFile);
            String[] children = dir.list();
            for (String filename : children) {
                gatherDirectoryFiles(new File(dir, filename), destinationDirectory, prefixLng);
            }
        } else {
            String toFile = destinationDirectory + (fromFile.substring(prefixLng));
            logger.debug("File: " + fromFile);
            logger.debug(" =-> " + toFile);
            if (logger.isDebugEnabled()) {
                logger.debug("gatherDirectoryFiles() - .");
            }
            FileUtils.copyFile(new File(fromFile), new File(toFile));
            fixUris(toFile);
        }
    }
