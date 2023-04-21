    public void backupDatabases(String databaseBackupDirectory, List<AppConfig> apps) throws Throwable {
        FileUtil util = new FileUtil();
        File file = util.getFile(databaseBackupDirectory);
        if (file.exists() && !file.isDirectory()) throw new RuntimeException("There is an file named " + databaseBackupDirectory + " that isn't a directory. Can't backup the databases!!!");
        if (!file.exists()) file.mkdir();
        File backupDir = new File(file, "backup-" + getDate());
        backupDir.mkdir();
        File appsDir = new File(backupDir, "apps");
        appsDir.mkdir();
        for (AppConfig appConfig : apps) {
            File appDir = new File(appsDir, appConfig.getName());
            if (appConfig.getPersistenceProvider() != null) {
                appDir.mkdir();
                PersistenceProvider provider = PersistenceManagerProvider.getProviders().get(appConfig.getPersistenceProvider());
                File db = provider.getDatabaseFile();
                util.copyFile(db, new File(appDir, db.getName()));
            }
            List<ModConfig> mods = appConfig.getModules();
            boolean modulePersistenceExists = false;
            for (ModConfig modConfig : mods) {
                if (modConfig.getPersistenceProvider() != null) {
                    modulePersistenceExists = true;
                    break;
                }
            }
            if (modulePersistenceExists) {
                if (!appDir.exists()) appDir.mkdir();
                File modsDir = new File(appDir, "modules");
                modsDir.mkdir();
                for (ModConfig modConfig : mods) {
                    if (modConfig.getPersistenceProvider() != null) {
                        File modDir = new File(modsDir, modConfig.getName());
                        modDir.mkdir();
                        PersistenceProvider provider = PersistenceManagerProvider.getProviders().get(modConfig.getPersistenceProvider());
                        File db = provider.getDatabaseFile();
                        util.copyFile(db, new File(modDir, db.getName()));
                    }
                }
            }
        }
    }
