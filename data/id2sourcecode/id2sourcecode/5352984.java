    private OpReportManager(OpProjectSession session) {
        session.addResourceInterceptor(this);
        this.jasperReports = new HashMap();
        this.expressFilePathCache = new HashMap();
        reportsDir = new File(REPORT_JAR_PATH);
        if (!reportsDir.isDirectory()) {
            reportsDir.mkdir();
            File readme = new File(reportsDir, "readme.txt");
            try {
                readme.createNewFile();
                FileWriter fw = new FileWriter(readme);
                fw.write(REPORT_JAR_PATH_READMESTRING);
                fw.flush();
                fw.close();
            } catch (Exception e) {
                logger.warn("Cannot write reports readme file", e);
            }
        }
        this.localeId = session.getLocale().getID();
        this.userSpecificString = (new Long(session.getUserID())).toString().concat(XEnvironmentManager.FILE_SEPARATOR);
        String tempFilename = REPORT_JAR_EXPRESSPATH_BASE;
        File tempFile = new File(tempFilename);
        if (!tempFile.exists()) {
            tempFile.mkdir();
        }
        tempFilename = REPORT_JAR_WORKINGPATH_BASE;
        tempFile = new File(tempFilename);
        if (!tempFile.exists()) {
            tempFile.mkdir();
        }
        tempFilename = REPORT_JAR_EXPRESSPATH_BASE.concat(XEnvironmentManager.FILE_SEPARATOR).concat(userSpecificString);
        tempFile = new File(tempFilename);
        if (!tempFile.exists()) {
            tempFile.mkdir();
        }
        tempFilename = REPORT_JAR_WORKINGPATH_BASE.concat(XEnvironmentManager.FILE_SEPARATOR).concat(userSpecificString);
        tempFile = new File(tempFilename);
        if (!tempFile.exists()) {
            tempFile.mkdir();
        }
        restoreState();
    }
