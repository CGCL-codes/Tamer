            public void handleMessage(IRCMessageEvent e) {
                forwardMessage(e, TopicReply.getChannel(e.getMessage()));
                e.consume();
                fireMessageProcessedEvent(e.getMessage());
            }
