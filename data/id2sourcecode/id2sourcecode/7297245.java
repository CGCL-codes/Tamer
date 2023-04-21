    public static void createBugReport(File reportFile, Throwable exception, String userMessage, Process process, String logMessage, File[] attachments) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(reportFile));
        zipOut.setComment("RapidMiner bug report - generated " + new Date());
        write("message.txt", "User message", userMessage, zipOut);
        write("_process.xml", "Process as in memory.", process.getRootOperator().getXML(false), zipOut);
        if (process.getProcessLocation() != null) {
            try {
                String contents = process.getProcessLocation().getRawXML();
                write(process.getProcessLocation().getShortName(), "Raw process file in repository.", contents, zipOut);
            } catch (Throwable t) {
                write(process.getProcessLocation().getShortName(), "Raw process file in repository.", "could not read: " + t, zipOut);
            }
        }
        write("_log.txt", "Log message", logMessage, zipOut);
        write("_properties.txt", "System properties, information about java version and operating system", getProperties(), zipOut);
        write("_exception.txt", "Exception stack trace", getStackTrace(exception), zipOut);
        for (File attachment : attachments) writeFile(attachment, zipOut);
        zipOut.close();
    }
