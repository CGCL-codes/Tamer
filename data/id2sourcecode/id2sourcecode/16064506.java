    private void copyBaseComponents() {
        try {
            localDestDir.mkdirs();
            FileUtils.copyFile(localSrc, new File(localDestDir + File.separator + "local.xml"));
            FileUtils.copyFile(dataSourceSrc, new File(destinationDirectory + File.separator + "datasource.xml"));
            for (File file : localSrcDir.listFiles()) {
                if (file.isDirectory()) {
                    if (file.getName().equals("limbo")) {
                        System.err.println("Limbo directory found, and has been excluded from the conversion");
                    }
                    if (file.getName().equals("orphans")) {
                        System.err.println("Orphan directory found, and has been excluded from the conversion");
                    }
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Problem copying the base components", e);
        }
    }
