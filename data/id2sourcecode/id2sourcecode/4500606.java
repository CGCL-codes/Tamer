    public void upsertFriend(final Agent friend) {
        if (!SwingUtilities.isEventDispatchThread()) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    /**
					 * Called when the thread is executed.
					 */
                    @Override
                    public void run() {
                        upsertFriend(friend);
                    }
                });
            } catch (Exception ex) {
                if (Whisper.isDebugging()) {
                    ex.printStackTrace();
                }
            }
        } else {
            synchronized (friends) {
                if (!friends.containsKey(friend.getUUID())) {
                    friends.put(friend.getUUID(), friend);
                } else {
                    Agent retrievedFriend = friends.get(friend.getUUID());
                    retrievedFriend.setName(friend.getName());
                    if (retrievedFriend.getOnline() != friend.getOnline()) {
                        retrievedFriend.setOnline(friend.getOnline());
                        Channel c = getChannel(friend.getName());
                        if (c != null) {
                            c.receiveInformationalMessage(friend.getName() + " is now " + (friend.getOnline() ? "online" : "offline"));
                        }
                    }
                }
            }
            updateObservers(false);
        }
    }
