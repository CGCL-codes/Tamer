    public void execute() throws MojoExecutionException {
        this.checkConfig();
        File confDirectory = new File(sgsHome, CONF);
        this.checkDirectory(confDirectory);
        try {
            if (sgsBoot != null) {
                this.checkFile(sgsBoot);
                File targetSgsBoot = new File(confDirectory, SGS_BOOT);
                this.getLog().info("Copying " + sgsBoot + " to " + targetSgsBoot);
                FileUtils.copyFile(sgsBoot, targetSgsBoot);
            }
            if (sgsServer != null) {
                this.checkFile(sgsServer);
                File targetSgsServer = new File(confDirectory, SGS_SERVER);
                this.getLog().info("Copying " + sgsServer + " to " + targetSgsServer);
                FileUtils.copyFile(sgsServer, targetSgsServer);
            }
            if (sgsLogging != null) {
                this.checkFile(sgsLogging);
                File targetSgsLogging = new File(confDirectory, SGS_LOGGING);
                this.getLog().info("Copying " + sgsLogging + " to " + targetSgsLogging);
                FileUtils.copyFile(sgsLogging, new File(confDirectory, SGS_LOGGING));
            }
        } catch (IOException e) {
            throw new MojoExecutionException("File copy failed", e);
        }
    }
