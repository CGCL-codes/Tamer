            public void run() {
                window.updateUsersList();
                window.insertDefault("*** " + je.getNick() + " [" + je.getUserName() + "@" + je.getHostName() + "] has joined " + je.getChannelName());
            }
