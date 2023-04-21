    private void doSave(File scenarioDir, boolean saveParams, boolean saveModelSpec) {
        if (scenarioDir != null) {
            File backup = null;
            try {
                if (!scenarioDir.getAbsolutePath().endsWith(".rs")) {
                    scenarioDir = new File(scenarioDir + ".rs");
                }
                if (scenarioDir.exists()) {
                    backup = FileUtils.backupDir(scenarioDir);
                    if (saveModelSpec) {
                        FileUtils.deleteIgnoreVC(scenarioDir);
                    } else {
                        FileUtils.deleteIgnoreVC(scenarioDir, ScenarioConstants.LEGACY_SCORE_FILE_NAME, ScenarioConstants.CONTEXT_FILE_NAME, ScenarioConstants.USER_PATH_FILE_NAME, ScenarioConstants.DEFAULT_FRAME_LAYOUT);
                    }
                    scenarioDir.mkdir();
                } else {
                    scenarioDir.mkdirs();
                }
                ScenarioSaver scenarioSaver = new ScenarioSaver(controller.getControllerRegistry(), scenarioDir, scenario.getContext());
                scenarioSaver.save(actionExts, scenario, saveModelSpec);
                scenario.setScenarioDirectory(scenarioDir);
                File paramFile = new File(scenarioDir, "parameters.xml");
                ParametersWriter pw = new ParametersWriter();
                pw.writeSpecificationToFile(paramsManager.getParameters(), paramFile);
                if (backup != null) {
                    File source = new File(backup, "plugin_jpf.xml");
                    if (source.exists()) FileUtils.copyFile(source, new File(scenarioDir, "plugin_jpf.xml"));
                    source = new File(backup, "styles");
                    if (source.exists()) FileUtils.copyDirs(source, new File(scenarioDir, "styles"));
                    FileUtils.delete(backup);
                }
            } catch (IOException e) {
                if (backup != null) restoreScenario(backup, scenarioDir);
                msgCenter.error("Error while saving scenario", e);
            } catch (ParseErrorException e) {
                if (backup != null) restoreScenario(backup, scenarioDir);
                msgCenter.error("Error while saving scenario", e);
            } catch (Exception e) {
                if (backup != null) restoreScenario(backup, scenarioDir);
                msgCenter.error("Error while saving scenario", e);
            }
        }
    }
