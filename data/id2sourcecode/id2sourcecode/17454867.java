    public void saveCurrentParameters() {
        try {
            if (scenario != null) {
                File paramFile = new File(scenario.getScenarioDirectory(), "parameters.xml");
                if (paramFile.exists()) {
                    FileUtils.copyFile(paramFile, new File(paramFile.getParentFile(), "parameters_backup.xml"));
                }
                ParametersWriter pw = new ParametersWriter();
                pw.writeSpecificationToFile(paramsManager.getParameters(), paramFile);
            }
        } catch (Exception ex) {
            msgCenter.error("Error while saving current scenario parameters.", ex);
        }
    }
