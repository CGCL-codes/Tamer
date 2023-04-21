    public void createInitialResources(File pRootDirectory, File pJBoxHomeDir) throws IOException {
        log.info("Importing resources from " + pRootDirectory.getAbsolutePath() + " to " + pJBoxHomeDir.getAbsolutePath());
        File lResourcesDirectory = new File(pRootDirectory, "resources");
        log.info("ResourcesDir: " + lResourcesDirectory.isDirectory());
        boolean lFoundCSS = false;
        boolean lFoundImages = false;
        boolean lFoundJS = false;
        if (lResourcesDirectory.isDirectory()) {
            for (final File lDir : lResourcesDirectory.listFiles()) {
                if (lDir.isDirectory()) {
                    if (lDir.getName().equalsIgnoreCase("css")) {
                        if (!(new File(pJBoxHomeDir, lDir.getName()).exists())) {
                            FileUtils.copyDirectory(lDir, new File(pJBoxHomeDir, lDir.getName()));
                            log.info("Copied CSS directory to destination...");
                        } else {
                            log.info(new File(pJBoxHomeDir, lDir.getName()).getAbsolutePath() + " exists!");
                        }
                        lFoundCSS = true;
                    }
                    if (lDir.getName().equalsIgnoreCase("images")) {
                        if (!(new File(pJBoxHomeDir, lDir.getName()).exists())) {
                            FileUtils.copyDirectory(lDir, new File(pJBoxHomeDir, lDir.getName()));
                            log.info("Copied Images directory to destination...");
                        } else {
                            log.info(new File(pJBoxHomeDir, lDir.getName()).getAbsolutePath() + " exists!");
                        }
                        lFoundImages = true;
                    }
                    if (lDir.getName().equalsIgnoreCase("js")) {
                        if (!(new File(pJBoxHomeDir, lDir.getName()).exists())) {
                            FileUtils.copyDirectory(lDir, new File(pJBoxHomeDir, lDir.getName()));
                            log.info("Copied JS directory to destination...");
                        } else {
                            log.info(new File(pJBoxHomeDir, lDir.getName()).getAbsolutePath() + " exists!");
                        }
                        lFoundJS = true;
                    }
                }
            }
        }
        if (lFoundCSS && lFoundImages && lFoundJS) {
            log.info("Found directory structure as needed... Installed files to gallery!");
        } else {
            log.error("Missing directories! Please unzip lightbox-zipfile to resources/ directory.");
            System.exit(1);
        }
    }
