    private boolean hasLegacyDesignerUIState(PathConfig pathConfig, File panelsObjFile) throws ConfigurationException {
        try {
            return panelsObjFile.exists();
        } catch (SecurityException e) {
            throw new ConfigurationException("Security manager denied access to " + panelsObjFile.getAbsoluteFile() + ". File read/write access must be enabled to " + pathConfig.tempFolder() + ".", e);
        }
    }
