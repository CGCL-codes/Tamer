    public void processMessage(Message m, Session session) {
        ChannelManager channelManager = session.getServerFacilities().getChannelManager();
        switch(m.getType()) {
            case MessageTypes.CLIENT_CHANNEL_JOIN:
                {
                    String channelName = m.getDataString(0);
                    String nickName = session.getUserState().getUser().getNickname();
                    StringBuffer topicMsg = new StringBuffer(64);
                    boolean channelExists = channelManager.getChannel(channelName) != null;
                    OutboundMessageQueue queue = session.getOutboundMessageQueue();
                    if (channelManager.isAtCapacity(channelName)) {
                        try {
                            queue.queueMessage(atCapacityError);
                        } catch (InvalidatedQueueException iqe) {
                        }
                    } else if (channelName.indexOf(" ") != -1) {
                        try {
                            queue.queueMessage(channelNameError);
                        } catch (InvalidatedQueueException iqe) {
                        }
                    } else if (channelExists && !meetsLevelRequirement(channelManager.getChannel(channelName).getLevel(), session.getUserState().getUser().getLevel())) {
                        try {
                            queue.queueMessage(permissionError);
                        } catch (InvalidatedQueueException iqe) {
                        }
                    } else {
                        channelManager.addUserToChannel(channelName, nickName);
                        channelManager.notifyChannelOfUserAction(ChannelManager.ACTION_JOIN, channelName, nickName);
                        topicMsg.append(channelName);
                        topicMsg.append(" ");
                        topicMsg.append(channelManager.getChannel(channelName).getTopic());
                        try {
                            queue.queueMessage(new Message(MessageTypes.SERVER_CHANNEL_JOIN_ACK, channelName));
                            queue.queueMessage(new Message(MessageTypes.CHANNEL_TOPIC, topicMsg.toString()));
                        } catch (InvalidatedQueueException iqe) {
                        }
                        channelManager.sendChannelList(session, channelName, ChannelManager.NON_USER_INITIATED);
                    }
                    break;
                }
            case MessageTypes.CLIENT_CHANNEL_LEAVE:
                {
                    String channelName = m.getDataString(0);
                    String nickName = session.getUserState().getUser().getNickname();
                    channelManager.removeUserFromChannel(channelName, nickName);
                    channelManager.notifyChannelOfUserAction(ChannelManager.ACTION_LEAVE, channelName, nickName);
                    break;
                }
            case MessageTypes.CLIENT_CHANNEL_PUBLIC_MESSAGE:
                {
                    String channelName = m.getDataString(0);
                    String textToSend = m.getDataString();
                    textToSend = textToSend.substring(textToSend.indexOf(' ') + 1, textToSend.length());
                    channelManager.sendMessage(channelName, session.getUserState().getUser().getNickname(), textToSend);
                    break;
                }
            case MessageTypes.LIST_CHANNELS:
                {
                    Channel[] channels = channelManager.getChannels();
                    OutboundMessageQueue queue = session.getOutboundMessageQueue();
                    if (channels != null) {
                        for (int x = 0; x < channels.length; x++) {
                            StringBuffer buf = new StringBuffer(128);
                            Channel c = channels[x];
                            buf.append(c.getName());
                            buf.append(" ");
                            buf.append(c.numMembers());
                            buf.append(" ");
                            buf.append(c.getTopic());
                            try {
                                queue.queueMessage(new Message(MessageTypes.LIST_CHANNELS_ENTRY, buf.toString()));
                            } catch (InvalidatedQueueException iqe) {
                            }
                        }
                    }
                    try {
                        queue.queueMessage(new Message(MessageTypes.LIST_CHANNELS, ""));
                    } catch (InvalidatedQueueException iqe) {
                    }
                    break;
                }
            case MessageTypes.CHANNEL_EMOTE:
                {
                    String channelName = m.getDataString(0);
                    String emoteText = m.getDataString(1);
                    String user = session.getUserState().getUser().getNickname();
                    StringBuffer buf = new StringBuffer(128);
                    buf.append(channelName);
                    buf.append(" ");
                    buf.append(user);
                    buf.append(" \"");
                    buf.append(emoteText);
                    buf.append("\"");
                    channelManager.sendMessage(channelName, new Message(MessageTypes.CHANNEL_EMOTE, buf.toString()));
                    break;
                }
            case MessageTypes.CHANNEL_TOPIC:
                {
                    String channelName = m.getDataString(0);
                    StringBuffer topic = new StringBuffer(128);
                    String wholeStr = m.getDataString();
                    StringBuffer topicMsgToSend = new StringBuffer(128);
                    topicMsgToSend.append(channelName);
                    topicMsgToSend.append(" ");
                    topicMsgToSend.append(wholeStr.substring(wholeStr.indexOf(' ') + 1, wholeStr.length()));
                    channelManager.sendMessage(channelName, new Message(MessageTypes.CHANNEL_TOPIC, topicMsgToSend.toString()));
                    break;
                }
            case MessageTypes.CLIENT_CHANNEL_USER_LIST:
                {
                    String channelName = m.getDataString(0);
                    channelManager.sendChannelList(session, channelName, ChannelManager.USER_INITIATED);
                    break;
                }
        }
    }
