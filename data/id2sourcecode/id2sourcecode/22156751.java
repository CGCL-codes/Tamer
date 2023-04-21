        public void onJoinCommand(JoinCommand command) {
            if (command.getSender().equals(user)) {
                Channel channel = command.getChannel();
                if (!messagePanel.isTabExist(channel)) messagePanel.addTab(channel);
                contactPanel.addChannel(channel);
                messagePanel.selectTab(channel);
            } else {
                Channel channel = command.getChannel();
                User sender = command.getSender();
                contactPanel.userJoined(sender, channel);
                String message = WebIRC.eventMessages.userJoins(sender.getNickname());
                messagePanel.addMessage(channel, new MessageCommand(message, MessageCommand.TYPE_ENTER));
            }
        }
