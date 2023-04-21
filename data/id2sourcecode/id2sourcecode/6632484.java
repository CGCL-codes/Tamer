    private IRCCoreTest(String server, String user) {
        createWindow();
        try {
            io = new IRCCore(server, user, "genericCore", 6667);
            io.addIRCEventsListener(new IRCEventsAdaptor() {

                @Override
                public void newMessage(NewMessageEvent nme) {
                    putMessage(nme.getMessage());
                }

                @Override
                public void topicUpdated(TopicUpdatedEvent tue) {
                    System.out.println("New topic for: " + tue.getChannel() + " -> " + tue.getNewTopic());
                }

                @Override
                public void ping(PingEvent pe) {
                    io.sendRawIRC("PONG :" + pe.getQuery());
                }

                @Override
                public void userList(UserListEvent ule) {
                    System.out.println("New user list for:" + ule.getChannel() + " contains " + ule.getUserList().size() + " users");
                }
            });
        } catch (UnknownHostException uhe) {
            putMessage("Unable to reach server");
        }
        cp = new CommandProcessor("/", io);
    }
