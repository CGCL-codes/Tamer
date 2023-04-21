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
