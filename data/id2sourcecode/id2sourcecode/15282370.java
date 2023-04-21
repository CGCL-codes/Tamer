    private PrintWriter openLogForAppend(WebServerOptions options) {
        String usersFileDirectory = options.getStringOption(WebServerConstants.optDataDirectory);
        if (usersFileDirectory == null) {
            LOGGER.severe("Data Directory (for chat messages log file) is null! Define it in cf file!");
            System.exit(1);
        }
        String filename = "ChatLog-" + getChannelId() + ".txt";
        PrintWriter chatLog = null;
        try {
            boolean append = true;
            chatLog = new PrintWriter(new FileOutputStream(new File(usersFileDirectory, filename), append));
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Writing char messages file " + filename + "failed: FileNotFoundException: ", e);
        }
        return chatLog;
    }
