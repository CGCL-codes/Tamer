    private final void setUpOutputDir() {
        this.outputPath = this.config.controler().getOutputDirectory();
        if (this.outputPath.endsWith("/")) {
            this.outputPath = this.outputPath.substring(0, this.outputPath.length() - 1);
        }
        if (this.config.controler().getRunId() != null) {
            this.controlerIO = new ControlerIO(this.outputPath, this.scenarioData.createId(this.config.controler().getRunId()));
        } else {
            this.controlerIO = new ControlerIO(this.outputPath);
        }
        File outputDir = new File(this.outputPath);
        if (outputDir.exists()) {
            if (outputDir.isFile()) {
                throw new RuntimeException("Cannot create output directory. " + this.outputPath + " is a file and cannot be replaced by a directory.");
            }
            if (outputDir.list().length > 0) {
                if (this.overwriteFiles) {
                    System.out.flush();
                    log.warn("###########################################################");
                    log.warn("### THE CONTROLER WILL OVERWRITE FILES IN:");
                    log.warn("### " + this.outputPath);
                    log.warn("###########################################################");
                    System.err.flush();
                } else {
                    throw new RuntimeException("The output directory " + this.outputPath + " exists already but has files in it! Please delete its content or the directory and start again. We will not delete or overwrite any existing files.");
                }
            }
        } else {
            if (!outputDir.mkdirs()) {
                throw new RuntimeException("The output directory path " + this.outputPath + " could not be created. Check pathname and permissions!");
            }
        }
        File tmpDir = new File(this.controlerIO.getTempPath());
        if (!tmpDir.mkdir() && !tmpDir.exists()) {
            throw new RuntimeException("The tmp directory " + this.controlerIO.getTempPath() + " could not be created.");
        }
        File itersDir = new File(this.outputPath + "/" + DIRECTORY_ITERS);
        if (!itersDir.mkdir() && !itersDir.exists()) {
            throw new RuntimeException("The iterations directory " + (this.outputPath + "/" + DIRECTORY_ITERS) + " could not be created.");
        }
    }
