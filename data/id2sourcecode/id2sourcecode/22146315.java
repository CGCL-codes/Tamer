        public void recoverFrom(File checkpointDir, CheckpointRecovery recovery) throws Exception {
            File bdbDir = getBdbSubDirectory(checkpointDir);
            path = recovery.translatePath(path);
            FileUtils.copyFiles(bdbDir, new File(path));
        }
