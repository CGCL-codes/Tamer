        public void onPartCommand(PartCommand command) {
            if (command.getSender().equals(user)) {
                Collection channels = command.getChannels();
                String message = WebIRC.eventMessages.youPart();
                for (Iterator it = channels.iterator(); it.hasNext(); ) {
                    Channel channel = (Channel) it.next();
                    contactPanel.removeChannel(channel);
                    messagePanel.addMessage(channel, new MessageCommand(message, MessageCommand.TYPE_EXIT));
                }
            } else {
                User sender = command.getSender();
                Collection channels = command.getChannels();
                String message = WebIRC.eventMessages.userPart(sender.getNickname());
                for (Iterator it = channels.iterator(); it.hasNext(); ) {
                    Channel channel = (Channel) it.next();
                    contactPanel.userLeft(sender, channel);
                    messagePanel.addMessage(channel, new MessageCommand(message, MessageCommand.TYPE_EXIT));
                }
            }
        }
