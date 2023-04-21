    public static void backupMenuFile() throws IOException {
        FileUtils.copyFile(new File(CONFIG_PATH), new File(ConfigLoader.getConfig().getWebpath() + "admin/xml/backup/" + Utils.dateFormat("yyyy-MM-dd HH_mm_ss") + ".xml"));
    }
