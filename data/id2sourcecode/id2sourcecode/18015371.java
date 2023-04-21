    public void sendChannelList(Session s, String channel, int type) {
        int entryMsgType = MessageTypes.SERVER_CHANNEL_USER_LIST_ENTRY;
        int endListMsgType = MessageTypes.SERVER_CHANNEL_USER_LIST_END;
        if (type == USER_INITIATED) {
            entryMsgType = MessageTypes.SERVER_CHANNEL_USER_LIST_ENTRY_2;
            endListMsgType = MessageTypes.CLIENT_CHANNEL_USER_LIST;
        }
        Channel c = getChannel(channel);
        String[] members = c.getMembers();
        if (members != null) {
            OutboundMessageQueue queue = s.getOutboundMessageQueue();
            for (int x = 0; x < members.length; x++) {
                StringBuffer msg = new StringBuffer(64);
                String singleMember = members[x];
                UserState us = serverFacilities.searchForUserState(singleMember);
                if (us != null) {
                    int numShares = us.getShareCount();
                    int linkType = us.getLinkType();
                    msg.append(channel);
                    msg.append(" ");
                    msg.append(singleMember);
                    msg.append(" ");
                    msg.append(numShares);
                    msg.append(" ");
                    msg.append(linkType);
                    try {
                        queue.queueMessage(new Message(entryMsgType, msg.toString()));
                    } catch (InvalidatedQueueException iqe) {
                    }
                }
            }
            try {
                queue.queueMessage(new Message(endListMsgType, channel));
            } catch (InvalidatedQueueException iqe) {
            }
        }
    }
