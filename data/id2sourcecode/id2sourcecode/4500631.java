    public void processInstantMessage(final String name, final UUID fromUUID, final String message, final boolean online, final UUID sessionUUID) {
        final Channel c;
        final Agent friend = getFriends().get(fromUUID);
        if (friend != null) {
            if (online && name.toLowerCase().equals(friend.getName().toLowerCase())) friend.setOnline(true);
        }
        final Group group = getGroups().get(sessionUUID);
        if (group != null) {
            c = new ChannelGroup(group.getName(), mainWindow, true);
            c.setUUID(sessionUUID);
            new RequestGroupChatJoin(connection, sessionUUID).execute();
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    /**
					 * Called when the thread is run.
					 */
                    @Override
                    public void run() {
                        synchronized (groups) {
                            mainWindow.addChannel(c, false);
                            mainWindow.updatePendingMessages(sessionUUID);
                            c.receiveMessage(new Message(message, name, fromUUID, online));
                        }
                    }
                });
            } catch (Exception ex) {
                if (Whisper.isDebugging()) {
                    ex.printStackTrace();
                }
            }
            return;
        }
        if (getChannel(fromUUID) != null) {
            c = getChannel(fromUUID);
        } else {
            c = new ChannelPrivate(name, mainWindow, true);
            c.setUUID(fromUUID);
        }
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                /**
				 * Called when the thread is run.
				 */
                @Override
                public void run() {
                    synchronized (friends) {
                        if (friend != null) {
                            friend.setTyping(false);
                        }
                        mainWindow.addChannel(c, false);
                        mainWindow.updatePendingMessages(fromUUID);
                        c.receiveMessage(new Message(message, name, fromUUID, online));
                        mainWindow.setTabIconForChannel(c, Resources.ICON_PENDING_MESSAGES, true);
                    }
                }
            });
        } catch (Exception ex) {
        }
    }
