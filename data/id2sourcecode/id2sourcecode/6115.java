            public boolean onEvent(SKJMessage msg) {
                logger.fine("Got the ACK for channel groups");
                SKJQueryAllChannelGroupsAck ack = (SKJQueryAllChannelGroupsAck) msg;
                String[] groups = ack.getChannelGroups();
                for (int i = 0; i < groups.length; i++) {
                    logger.finest("Channel Groups on switch: " + i + " = '" + groups[i] + "'");
                }
                app.setChannelGroups(groups);
                return true;
            }
