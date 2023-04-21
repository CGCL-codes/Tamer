    public void saveUserOptions(UserConfiguration userConfiguration) {
        CtuluLogGroup reloadUserConfiguration = getOptionsManager().validUserConfiguration(userConfiguration);
        if (reloadUserConfiguration.containsSomething()) {
            LogsDisplayer.displayError(reloadUserConfiguration, NbBundle.getMessage(ConfigurationManagerService.class, "ValidationOptions.BilanDialogTitle"));
        }
        if (!reloadUserConfiguration.containsFatalError()) {
            getOptionsManager().reloadUserConfiguration(userConfiguration);
            final CrueCONFReaderWriter reader = new CrueCONFReaderWriter("1.2");
            final CtuluLog log = new CtuluLog(BusinessMessages.RESOURCE_BUNDLE);
            Configuration conf = new Configuration();
            conf.setUser(userConfiguration);
            reader.writeXMLMetier(new CrueIOResu<Configuration>(conf), installationService.getUserConfigFile(), log, null);
            if (log.isNotEmpty()) {
                LogsDisplayer.displayError(log, NbBundle.getMessage(ConfigurationManagerService.class, "UserOptionsSave.BilanDialogTitle"));
            }
        }
    }
