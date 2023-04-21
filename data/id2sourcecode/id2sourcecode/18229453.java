        public void recoverFrom(File checkpointDir, CheckpointRecovery cr) throws Exception {
            target = new File(cr.translatePath(target.getAbsolutePath()));
            FileUtils.copyFile(new File(checkpointDir, backup), target);
        }
