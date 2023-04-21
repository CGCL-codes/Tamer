        public void recoverFrom(File checkpointDir, CheckpointRecovery recovery) throws Exception {
            mainConfig = recovery.translatePath(mainConfig);
            sheets = recovery.translatePath(sheets);
            File srcCfg = new File(checkpointDir, "config.txt");
            new File(mainConfig).getParentFile().mkdirs();
            FileUtils.copyFile(srcCfg, new File(mainConfig));
            File srcSheets = new File(checkpointDir, "sheets");
            FileUtils.copyFiles(srcSheets, new File(sheets));
        }
