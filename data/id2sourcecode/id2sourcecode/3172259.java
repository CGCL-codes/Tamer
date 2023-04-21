    @Override
    public boolean prepare() {
        log.debug("Preparing for " + this.getPipelineStep().getName());
        if (overwrite) deleteOldResults();
        String csv = this.setup.getTempTransitionFile();
        if (!isFileExist(csv)) return false;
        if (setup.getPepxmlFile() != null) {
            String pepxml = this.getPepxmlComputeServerPath();
            if (!isFileExist(pepxml)) return false;
        }
        for (TransitionListValidatorRunInfo runInfo : setup.getTlvRunInfos()) {
            List<String> lcmsFilesFullPath = getLcmsComputeServerPaths(runInfo.getSampleSetID());
            for (int i = 0; i < lcmsFilesFullPath.size(); i++) {
                String lcmsFile = lcmsFilesFullPath.get(i);
                if (!isFileExist(lcmsFile)) return false;
            }
        }
        if (!FileUtils.copyFile(csv, this.getTransitionInputPath())) return false;
        return true;
    }
