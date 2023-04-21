    public void processGroupMessage(Message message, final UUID sessionUUID) {
        final Channel c;
        if (getChannel(sessionUUID) == null) {
            Group tmpGroup = getGroups().get(sessionUUID);
            if (tmpGroup != null) {
                c = new ChannelGroup(tmpGroup.getName(), mainWindow, true);
                c.setUUID(sessionUUID);
                tmpGroup.addObserver((ChannelGroup) c);
                tmpGroup.updateObservers(false);
                c.receiveMessage(message);
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {

                        /**
						 * Called when the thread is run.
						 */
                        @Override
                        public void run() {
                            mainWindow.addChannel(c, false);
                            mainWindow.setTabIconForChannel(c, Resources.ICON_PENDING_MESSAGES, true);
                        }
                    });
                } catch (Exception e) {
                }
            }
        } else {
            c = getChannel(sessionUUID);
            c.receiveMessage(message);
            mainWindow.setTabIconForChannel(c, Resources.ICON_PENDING_MESSAGES, true);
        }
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                /**
				 * Called when the thread is run.
				 */
                @Override
                public void run() {
                    mainWindow.updatePendingMessages(sessionUUID);
                }
            });
        } catch (Exception e) {
        }
    }
