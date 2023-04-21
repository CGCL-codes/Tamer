    public void sendMessage(String channel, Message msg) {
        ServerFacilities serverFacilities = getServerFacilities();
        Channel c = getChannel(channel);
        if (c != null) {
            String[] members = c.getMembers();
            if (members != null) {
                int numMembers = members.length;
                for (int x = 0; x < numMembers; x++) {
                    Session targetSession = serverFacilities.searchForSession(members[x]);
                    if (targetSession != null) {
                        OutboundMessageQueue queue = targetSession.getOutboundMessageQueue();
                        try {
                            queue.queueMessage(msg);
                        } catch (InvalidatedQueueException iqe) {
                        }
                    }
                }
            }
        }
    }
