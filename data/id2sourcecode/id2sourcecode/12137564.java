            public void handleMessage(IRCMessageEvent e) {
                forwardMessage(e, TopicsetReply.getChannel(e.getMessage()));
                e.consume();
                fireMessageProcessedEvent(e.getMessage());
            }
