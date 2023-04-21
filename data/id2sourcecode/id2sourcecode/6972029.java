    public void channelListReceived(ChannelsEvent evt) {
        int[] channels = evt.getChannelsNumbers();
        if (firstTime) {
            for (int i = 0; i < channels.length; i++) {
                Console chatConsole = new Console(getConn(), consolePreferences, ("tell " + channels[i]));
                chatConsoles.put(channels[i], chatConsole);
                mainPane.addTab(Integer.toString(channels[i]), nullIcon, chatConsole);
                channelSet.add(new Integer(channels[i]));
            }
            for (int j = 0; j < 3; j++) {
                if (j == 0 && getPrefs().getBool("tabs.shout", true)) {
                    Console chatConsole = new Console(getConn(), consolePreferences, "shout");
                    chatConsoles.put(new Integer(500), chatConsole);
                    mainPane.addTab("shouts", nullIcon, chatConsole);
                } else if (j == 1 && getPrefs().getBool("tabs.cshout", true)) {
                    Console chatConsole = new Console(getConn(), consolePreferences, "cshout");
                    chatConsoles.put(new Integer(501), chatConsole);
                    mainPane.addTab("cshouts", nullIcon, chatConsole);
                } else if (getPrefs().getBool("tabs.plain", true)) {
                    Console chatConsole = new Console(getConn(), consolePreferences, "plain");
                    chatConsoles.put(new Integer(502), chatConsole);
                    mainPane.addTab("plain text", nullIcon, chatConsole);
                }
            }
            firstTime = false;
        } else {
            for (int i = 0; i < channels.length; i++) {
                if (channelSet.add(new Integer(channels[i]))) {
                    updateChannelsView(false, channels[i]);
                } else {
                }
            }
        }
        listReceived = true;
    }
