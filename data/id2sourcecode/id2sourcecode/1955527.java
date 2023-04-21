    public void run() {
        if (logDirectory.exists() && logDirectory.isDirectory()) {
            File[] taskReports = logDirectory.listFiles(new PrefixExtentionFilter(MailLogger.TASK_FILE_PRAFIX, MailLogger.XML_EXTANTION));
            for (int i = 0, len = taskReports.length; i < len; i++) {
                if (!taskReports[i].getName().contains(RESTORE_PREFIX)) {
                    if (checkTaskFromLog(taskReports[i])) {
                        MailTask mailTask = new MailTask();
                        mailTask.setMailService(getService());
                        mailTask.setConfigElements(getConfigs());
                        mailTask.setConfigName(MailTask.ALL_CONFIGS_IDENTIFICATOR);
                        mailTask.setTemplateHref(getTemplate());
                        mailTask.setTaskId(getTaskId());
                        HashMap<String, Object> restorateMap = new HashMap<String, Object>();
                        restorateMap.put(MailTask.LAST_CONFIG, getLastConfig());
                        restorateMap.put(MailTask.LAST_OBJECT, getLastObject());
                        restorateMap.put(MailTask.LAST_RECORD, getLastRecord());
                        restorateMap.put(MailTask.QUERY, getQuery());
                        mailTask.setRestoreMap(restorateMap);
                        try {
                            mailTask.start();
                        } catch (MailException e) {
                            logger.info("MailRestorer: Cannot start mail sending task - " + e.getMessage());
                        }
                        taskReports[i].delete();
                    }
                }
            }
        }
    }
