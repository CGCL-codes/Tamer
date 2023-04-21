    public void copyQualityModelsToFramework() throws IOException {
        deleteQualityModelsFromFramework();
        File fwRepo = new File(getFrameworkQualityModelRepoDirectory());
        if (!fwRepo.isDirectory() || !fwRepo.canWrite()) throw new IOException("Cannot write to quality model repository: " + fwRepo.getAbsolutePath());
        if (getQualityModelDir() != null) {
            File qualityModelDir = new File(getQualityModelDir());
            if (!qualityModelDir.isDirectory()) throw new IOException("Directory with quality models (" + getQualityModelDir() + ") not found.");
            for (File model : qualityModelDir.listFiles()) {
                if (model.isDirectory()) {
                    if (AuxFiles.doesDirContainFile(fwRepo, model.getName())) {
                        throw new IOException("The quality model " + model.getName() + " already exists in the framework.");
                    }
                    File modelInRepo = new File(fwRepo.getAbsoluteFile() + File.separator + model.getName());
                    modelInRepo.mkdir();
                    for (File modelFile : model.listFiles()) {
                        if (!modelFile.isDirectory()) AuxFiles.copyFile(modelFile, modelInRepo);
                    }
                    File modelFlag = new File(modelInRepo.getAbsoluteFile() + File.separator + FLAG_FILE_NAME);
                    modelFlag.createNewFile();
                }
            }
        }
    }
