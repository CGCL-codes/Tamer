    public void update(Observable o, Object arg) {
        if (arg instanceof IRCEvent) {
            IRCEvent event = (IRCEvent) arg;
            if (event.getType() == Type.CONNECT_COMPLETE) {
                System.out.println("CONNECT_COMPLETE");
                System.out.println("chat: " + chatPanel);
                chatPanel.enableControls();
                handleApplet.setLoadingFlag(false);
            } else if (event.getType() == Type.JOIN_COMPLETE) {
                System.out.println("JOIN_COMPLETE");
                JoinCompleteEvent joinCompleteEvent = (JoinCompleteEvent) event;
                Channel currentChannel = joinCompleteEvent.getChannel();
                chatPanel.populateConnectedUsers(currentChannel);
            } else if (event.getType() == Type.CHANNEL_MESSAGE) {
                MessageEvent messageEvent = (MessageEvent) event;
                if (messageEvent.getMessage().startsWith("QUITCHAT")) {
                    chatPanel.removeUserTable(messageEvent.getNick());
                    chatPanel.removeUserHost(messageEvent.getHostName());
                    String tempStr = messageEvent.getNick() + " has left ";
                    StaticData.chatMessage = tempStr;
                    String color = chatPanel.getColor(messageEvent.getNick().trim());
                    if (messageEvent.getNick().equals(StaticData.teacher)) chatPanel.updateMainContentArea(tempStr, color, true); else chatPanel.updateMainContentArea(tempStr, color, false);
                    return;
                }
                String tempStr = messageEvent.getNick() + ": " + messageEvent.getMessage();
                StaticData.chatMessage = tempStr;
                String color = chatPanel.getColor(messageEvent.getNick().trim());
                if (messageEvent.getNick().trim().equals(StaticData.teacher)) chatPanel.updateMainContentArea(tempStr, color, true); else chatPanel.updateMainContentArea(tempStr, color, false);
            } else if (event.getType() == Type.PRIVATE_MESSAGE) {
                System.out.println("PRIVATE_MESSAGE");
                MessageEvent messageEvent = (MessageEvent) event;
                String what = messageEvent.getMessage();
                String whom = messageEvent.getNick().trim();
                handleApplet.updatePvt(whom, whom + ": " + what);
                handleApplet.setTabForegroundColor(messageEvent.getNick().trim(), 2);
            } else if (event.getType() == Type.JOIN) {
                System.out.println("JOIN");
                JoinEvent joinEvent = (JoinEvent) event;
                chatPanel.addUserTable(joinEvent.getNick());
                chatPanel.addUserHost(joinEvent.getHostName());
                System.out.println("host: " + joinEvent.getHostName());
            } else if (event.getType() == Type.QUIT) {
                System.out.println("QUIT");
                QuitEvent quitEvent = (QuitEvent) event;
                chatPanel.removeUserTable(quitEvent.getNick());
                chatPanel.removeUserHost(quitEvent.getHostName());
                String tempStr = quitEvent.getNick() + " has left (" + quitEvent.getQuitMessage() + ")";
                StaticData.chatMessage = tempStr;
                String color = chatPanel.getColor(quitEvent.getNick().trim());
                if (quitEvent.getNick().equals(StaticData.teacher)) chatPanel.updateMainContentArea(tempStr, color, true); else chatPanel.updateMainContentArea(tempStr, color, false);
            } else if (event.getType() == Type.WHOIS_EVENT) {
            }
        }
    }
