    private boolean refactor(File file, EntityManagerFactory photoElementsEmf, EntityManagerFactory cahayaEmf) throws IOException {
        myLog.debug("refactor (" + file + ")");
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles(myMetaDataFilter);
                for (int i = 0; i < files.length; i++) {
                    if (isRecursive() || !files[i].isDirectory()) {
                        boolean result = refactor(files[i], photoElementsEmf, cahayaEmf);
                        if (!result) {
                            return false;
                        }
                    }
                }
                if (myMakeChanges) {
                    files = file.listFiles(myMetaDataFilter);
                    if (files.length == 0) {
                        myLog.info("deleting " + file);
                        FileUtil.deleteDirectory(file);
                    } else {
                        myLog.info("length = " + files.length + " for " + file);
                    }
                }
            } else {
                boolean result = process(file, photoElementsEmf, cahayaEmf);
                if (!result) {
                    return false;
                }
            }
            return true;
        } else {
            myLog.error(file + " does not exist");
            return false;
        }
    }
