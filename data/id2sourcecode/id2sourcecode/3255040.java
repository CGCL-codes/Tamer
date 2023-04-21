    private final void setUpOutputDir() {
        outputPath = this.config.controler().getOutputDirectory();
        if (outputPath.endsWith("/")) {
            outputPath = outputPath.substring(0, outputPath.length() - 1);
        }
        if (this.config.controler().getRunId() != null) {
            this.controlerIO = new ControlerIO(outputPath, this.scenarioData.createId(this.config.controler().getRunId()));
        } else {
            this.controlerIO = new ControlerIO(outputPath);
        }
        File outputDir = new File(outputPath);
        if (outputDir.exists()) {
            if (outputDir.isFile()) {
                throw new RuntimeException("Cannot create output directory. " + outputPath + " is a file and cannot be replaced by a directory.");
            }
            if (outputDir.list().length > 0) {
                if (this.overwriteFiles) {
                    log.warn("###########################################################");
                    log.warn("### THE CONTROLER WILL OVERWRITE FILES IN:");
                    log.warn("### " + outputPath);
                    log.warn("###########################################################");
                } else {
                    throw new RuntimeException("The output directory " + outputPath + " exists already but has files in it! Please delete its content or the directory and start again. We will not delete or overwrite any existing files.");
                }
            }
        } else {
            if (!outputDir.mkdirs()) {
                throw new RuntimeException("The output directory path " + outputPath + " could not be created. Check pathname and permissions!");
            }
        }
        File tmpDir = new File(this.controlerIO.getTempPath());
        if (!tmpDir.mkdir() && !tmpDir.exists()) {
            throw new RuntimeException("The tmp directory " + this.controlerIO.getTempPath() + " could not be created.");
        }
        File itersDir = new File(outputPath + "/" + DIRECTORY_ITERS);
        if (!itersDir.mkdir() && !itersDir.exists()) {
            throw new RuntimeException("The iterations directory " + (outputPath + "/" + DIRECTORY_ITERS) + " could not be created.");
        }
    }
