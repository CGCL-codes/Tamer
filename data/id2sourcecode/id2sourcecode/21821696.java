    private void backup(String mode) {
        Boolean makeBackup = configuration.getBoolean(mode, Boolean.TRUE);
        if (makeBackup != null && makeBackup.booleanValue()) {
            if (checkIfBackupDirectoryExists(backupDataFileDirectory)) {
                String suffix = mode == Configuration.BACKUP_ON_STARTUP ? "Startup" : "Shutdown";
                Locale locale = timeSlotTracker.getLocale();
                DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
                Calendar calendar = Calendar.getInstance(locale);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                String dayOfWeekName = dateFormatSymbols.getWeekdays()[dayOfWeek];
                String backupName = "timeslottracker-backupOn" + suffix + "-" + dayOfWeekName + ".xml";
                String source = dataFileDirectory + TST_XML_FILENAME;
                if (new File(source).length() != 0) {
                    FileUtils.copyFile(source, backupDataFileDirectory + backupName, timeSlotTracker);
                }
            }
        }
    }
