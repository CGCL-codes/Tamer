    private static void copyToUserFolder(String fileName) {
        try {
            FileUtils.copyFile(new File(Main.getDataFolder() + File.separator + fileName), new File(Main.getUserFolder() + File.separator + fileName));
        } catch (Throwable t) {
            GuiLogger.getLogger().log(Level.SEVERE, "Error while copying " + fileName, t);
        }
    }
