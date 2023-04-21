    private void copyDir(File srcDir, File destDirParent, ResultList resultList, boolean continueOnErrors) {
        File destDir = new File(destDirParent, srcDir.getName());
        if (!destDir.mkdir()) {
            String message = "Could not create directory " + destDir;
            if (continueOnErrors) {
                resultList.addResult(new SyncResult(Result.Type.ERROR, message));
            } else {
                throw new SyncException(message);
            }
        }
        File[] files = srcDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                try {
                    FileUtils.copyFile(files[i], destDir);
                } catch (SyncException e) {
                    if (continueOnErrors) {
                        resultList.addResult(new SyncResult(Result.Type.ERROR, e.getMessage()));
                    } else {
                        throw e;
                    }
                }
            } else if (files[i].isDirectory()) {
                copyDir(files[i], destDir, resultList, continueOnErrors);
            } else {
                resultList.addResult(new SyncResult(Result.Type.WARNING, "Warning: " + files[i] + " is neither file nor directory; " + "ignoring it!"));
            }
        }
    }
