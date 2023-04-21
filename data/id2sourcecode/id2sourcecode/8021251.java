    @Override
    public void service(int svcId, final String fullCommand, final int channelId, final int msgId, final String source, final MessageListener output) {
        if (svcId == pingCmdId) {
            System.out.println("Pinging at channel: " + channelId);
            new Thread() {

                public void run() {
                    String result = ircInterface.ping(source);
                    if (result != null) {
                        System.out.println("Ping result at channel: " + channelId);
                        output.message(channelId, msgId, null, new Message(new String[] { Message.COLOR1, source, ", your ping is ", Message.COLOR2, result, Message.COLOR1, " secs" }));
                    }
                }
            }.start();
        } else if (svcId == serverCmdId) {
            output.message(channelId, msgId, null, new Message(Message.COLOR1, ircInterface.getServer()));
        } else if (svcId == joinCmdId) {
            final String finFullCommand = fullCommand;
            new Thread() {

                public void run() {
                    String tokenized[] = StringUtility.tokenize(fullCommand);
                    if (tokenized.length > 1) {
                        String channelName = tokenized[1];
                        if (!channelName.startsWith("#")) channelName = "#" + channelName;
                        int id = ircInterface.getChannelId(channelName);
                        if (id < 0) {
                            try {
                                id = ircInterface.joinChannel(channelName);
                                if (id >= 0) {
                                    try {
                                        Thread.sleep(5000);
                                    } catch (Exception e) {
                                    }
                                    if (ircInterface.ensureJoin(channelName)) output.message(channelId, msgId, null, new Message("Zorobot has joined " + channelName)); else {
                                        ircInterface.leaveChannel(ircInterface.getChannelId(channelName));
                                        output.message(channelId, msgId, null, new Message("Couldn't join " + channelName));
                                    }
                                } else {
                                    output.message(channelId, msgId, null, new Message("Zorobot has joined too many channel, please try again later"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                output.message(channelId, msgId, null, new Message(e.getMessage()));
                            }
                        } else {
                            output.message(channelId, msgId, null, new Message("Zorobot is already in that channel"));
                        }
                    }
                }
            }.start();
        } else if (svcId == leaveCmdId) {
            if (ircInterface.leaveChannel(channelId) < 0) output.message(channelId, msgId, null, new Message("You should never use this command in this channel ^^"));
        }
    }
