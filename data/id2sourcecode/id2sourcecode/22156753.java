        public void onKickCommand(KickCommand command) {
            String message;
            String whoNick = command.getSender().getNickname();
            User victim = command.getUser();
            final Channel channel = command.getChannel();
            String reason = command.getKickMessage();
            if (user.equals(victim)) {
                contactPanel.removeChannel(channel);
                message = WebIRC.eventMessages.youHaveBeenKickedMsg(whoNick, reason);
                String confirmMessage = WebIRC.eventMessages.youHaveBeenKicked(channel.toString(), whoNick, reason);
                new ConfirmDialog(confirmMessage, new Command() {

                    public void execute() {
                        sendCommand(new JoinCommand(channel));
                    }
                }).show();
            } else {
                contactPanel.userLeft(victim, channel);
                message = WebIRC.eventMessages.someoneHasBeenKicked(victim.getNickname(), whoNick, reason);
            }
            messagePanel.addMessage(channel, new MessageCommand(message, MessageCommand.TYPE_KICK));
        }
