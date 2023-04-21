    private void storeMessages() {
        String usersFileDirectory = options.getStringOption(WebServerConstants.optDataDirectory);
        if (usersFileDirectory == null) {
            LOGGER.severe("Data Directory (for chat messages file) is null! Define it in cf file!");
            System.exit(1);
        }
        String filename = "Chatmessages-" + getChannel().getChannelId() + ".txt";
        PrintWriter out = null;
        try {
            out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(usersFileDirectory, filename)), WebServerConstants.charset));
            for (ChatMessage msg : getLastNChatMessages()) {
                String line = makeLine(msg);
                out.println(line);
            }
            out.close();
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Writing char messages file " + filename + "failed: FileNotFoundException: ", e);
        }
    }
