    public File getLocalApplicationDirectory() {
        if (localApplicationDirectory != null) return localApplicationDirectory;
        if (isWebStartApplication()) {
            localApplicationDirectory = createTempAppDir();
            return localApplicationDirectory;
        }
        boolean appDirSetted = false;
        for (String appDirKey : appDir) {
            if (getProgramAruments().containsKey(appDirKey)) {
                File appDirFile = new File(getProgramAruments().get(appDirKey));
                localApplicationDirectory = appDirFile;
                appDirSetted = true;
                break;
            }
        }
        if (!appDirSetted && getProgramAruments().containsKey(ARGUMENT_FILE)) {
            File fArg = new File(getProgramAruments().get(ARGUMENT_FILE));
            localApplicationDirectory = fArg.getParentFile();
            if (localApplicationDirectory == null) localApplicationDirectory = getAppDirFromHome();
        } else if (!appDirSetted && getProgramAruments().containsKey(INIT_JS_TMPL)) {
            File fArg = new File(getProgramAruments().get(INIT_JS_TMPL));
            localApplicationDirectory = fArg.getParentFile();
            if (localApplicationDirectory == null) localApplicationDirectory = getAppDirFromHome();
        } else {
            localApplicationDirectory = getAppDirFromHome();
        }
        if (localApplicationDirectory == null) {
            localApplicationDirectory = createTempAppDir();
            if (localApplicationDirectory == null) return null;
        }
        if (!localApplicationDirectory.exists()) {
            if (!localApplicationDirectory.mkdirs()) {
                Logger.getLogger(ApplicationGlobal.class.getName()).log(Level.SEVERE, "can't create directory {0}", localApplicationDirectory);
                localApplicationDirectory = null;
                return null;
            }
        } else if (!localApplicationDirectory.isDirectory() || !localApplicationDirectory.canRead() || !localApplicationDirectory.canWrite()) {
            Logger.getLogger(ApplicationGlobal.class.getName()).log(Level.SEVERE, "can't read/write directory {0}", localApplicationDirectory);
            localApplicationDirectory = null;
            return null;
        }
        return localApplicationDirectory;
    }
