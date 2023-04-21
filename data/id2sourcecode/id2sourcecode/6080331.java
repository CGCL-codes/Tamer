            public void run() {
                win.updateUsersList();
                win.insertDefault("*** " + pe.getWho() + " [" + pe.getUserName() + "@" + pe.getHostName() + "] has left " + pe.getChannelName() + " [" + pe.getPartMessage() + "]");
            }
