    protected static void checkForFailedInstalls(UpdateManagerImpl manager) {
        try {
            File update_dir = new File(manager.getUserDir() + File.separator + UPDATE_DIR);
            File[] dirs = update_dir.listFiles();
            if (dirs != null) {
                boolean found_failure = false;
                String files = "";
                for (int i = 0; i < dirs.length; i++) {
                    File dir = dirs[i];
                    if (dir.isDirectory()) {
                        found_failure = true;
                        File[] x = dir.listFiles();
                        if (x != null) {
                            for (int j = 0; j < x.length; j++) {
                                files += (files.length() == 0 ? "" : ",") + x[j].getName();
                            }
                        }
                        FileUtil.recursiveDelete(dir);
                    }
                }
                if (found_failure) {
                    Logger.log(new LogAlert(LogAlert.UNREPEATABLE, LogAlert.AT_ERROR, MessageText.getString("Alert.failed.update", new String[] { files })));
                }
            }
        } catch (Throwable e) {
            Debug.printStackTrace(e);
        }
    }
