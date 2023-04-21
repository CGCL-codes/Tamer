            public void handleMessage(IRCMessageEvent e) {
                String channel = NosuchchannelError.getChannelname(e.getMessage());
                if (getChannel(channel) != null) {
                    forwardMessage(e, channel);
                    e.consume();
                    fireMessageProcessedEvent(e.getMessage());
                }
            }
