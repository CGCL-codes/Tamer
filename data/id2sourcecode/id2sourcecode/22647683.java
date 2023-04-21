    void addWriter(String logPath) {
        String separator = System.getProperty("file.separator");
        String beginning = "log" + separator + Utilities.sessionID + separator;
        String filePath;
        if (logPath.contains("/")) {
            filePath = beginning + logPath.substring(0, logPath.lastIndexOf("/")).replace("/", separator);
        } else {
            filePath = beginning;
        }
        new File(filePath).mkdirs();
        try {
            FileOutputStream outputStream = new FileOutputStream(beginning + logPath.replace("/", separator));
            outputStream.getChannel().lock();
            writers.put(logPath, new OutputStreamWriter(outputStream));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
