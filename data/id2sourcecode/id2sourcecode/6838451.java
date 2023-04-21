    private void restoreMessages() {
        String usersFileDirectory = options.getStringOption(WebServerConstants.optDataDirectory);
        if (usersFileDirectory == null) {
            LOGGER.severe("Data Directory (for chat messages file) is null! Define it in cf file!");
            System.exit(1);
        }
        String filename = "Chatmessages-" + getChannel().getChannelId() + ".txt";
        try {
            BufferedReader msgs = new BufferedReader(new InputStreamReader(new FileInputStream(new File(usersFileDirectory, filename)), WebServerConstants.charset));
            String line = null;
            while ((line = msgs.readLine()) != null) {
                if (line.matches("\\s*")) {
                } else {
                    parseMsgLine(line);
                }
            }
            msgs.close();
            LOGGER.info("Restored " + lastNChatMessages.size() + " messages from file.");
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Chat messages file " + filename + " not found!", e);
            System.exit(1);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IOException while reading chat messages file " + filename + "!", e);
            System.exit(1);
        }
    }
