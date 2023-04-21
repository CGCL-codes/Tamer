    public void checkpoint(File dir, List<RecoverAction> actions) throws IOException {
        File config = new File(dir, "config.txt");
        FileUtils.copyFile(mainConfig, config);
        File sheets = new File(dir, "sheets");
        FileUtils.copyFiles(sheetsDir, sheets);
        actions.add(new FSMRecover(mainConfig.getAbsolutePath(), sheetsDir.getAbsolutePath()));
    }
