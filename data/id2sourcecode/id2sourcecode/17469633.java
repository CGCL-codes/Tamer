    public void copySettings(File newOrderFileName, String newSettingsDirectory) throws IOException {
        File oldSettingsDirectory = getSettingsDirectory();
        orderFile = newOrderFileName;
        try {
            getOrder().setAttribute(new Attribute(CrawlOrder.ATTR_SETTINGS_DIRECTORY, newSettingsDirectory));
        } catch (Exception e) {
            throw new IOException("Could not update settings with new location: " + e.getMessage());
        }
        writeSettingsObject(getSettingsObject(null));
        File newDir = getPathRelativeToWorkingDirectory(newSettingsDirectory);
        FileUtils.copyFiles(oldSettingsDirectory, newDir);
    }
