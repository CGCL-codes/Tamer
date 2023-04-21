    public void run() {
        try {
            ParameterBackup export = new ParameterBackup();
            export.setDate(df.format(new Date()));
            List<Module> modules = new ArrayList<Module>();
            export.setModules(modules);
            for (Board currBoard : theLab.getAllBoardsFound()) {
                String moduleId = currBoard.getBoardIdentifier();
                backupUI.addMessage("Process Module " + moduleId);
                stdlog.log(Level.ALL, "Process Module " + moduleId);
                Module newModule = new Module();
                newModule.setAddress(currBoard.getAddress());
                newModule.setModuleid(currBoard.getBoardIdentifier());
                newModule.setCommChannelName(currBoard.getCommChannel().getChannelName());
                String identifier = currBoard.queryStringValue(254);
                identifier = stripStringAnswer(identifier);
                newModule.setIdentifier(identifier);
                List<ModuleParameter> parameterlist = new ArrayList<ModuleParameter>();
                newModule.setParameters(parameterlist);
                modules.add(newModule);
                Map<String, List<BackupConfig>> config = theLab.createBackupConfig();
                List<BackupConfig> configs = config.get(moduleId);
                if (configs == null) continue;
                for (BackupConfig currConfig : configs) {
                    int from = currConfig.getLow();
                    int to = from;
                    if (currConfig.getHigh() >= 0) to = currConfig.getHigh();
                    backupUI.addMessage("Process channels " + from + " to " + to + " " + currConfig.getDescription());
                    stdlog.log(Level.ALL, "Process channels " + from + " to " + to + " " + currConfig.getDescription());
                    for (int subchannel = from; subchannel <= to; subchannel++) {
                        ModuleParameter newParameter = new ModuleParameter();
                        newParameter.setDescription(currConfig.getDescription() + " (CH " + subchannel + ")");
                        newParameter.setSubchannel(subchannel);
                        String stringValue = currBoard.queryStringValue(subchannel);
                        stringValue = stripStringAnswer(stringValue);
                        newParameter.setValue(stringValue);
                        parameterlist.add(newParameter);
                    }
                }
            }
            BackupDataHandler handler = new BackupDataHandler();
            handler.storeConfig(export, destinationFile);
        } catch (Exception e) {
            backupUI.addMessage("Error in Backup. See Console for Detail");
            stdlog.log(Level.SEVERE, "Error occured in Restore", e);
        } finally {
            backupUI.addMessage("!!!!!!!!!!!!!!!!!\nBackup Finished\n!!!!!!!!!!!!!!!!!");
            backupUI.finishedBackup();
        }
    }
