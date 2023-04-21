        public void onModeCommand(ModeCommand command) {
            messagePanel.addSystemMessage(command);
            if (command.isChannelMode()) {
                Channel channel = command.getChannel();
                HashMap modes = command.getModes();
                for (Iterator it = modes.keySet().iterator(); it.hasNext(); ) {
                    MessageCommand msg;
                    Character mode = (Character) it.next();
                    String argument = (String) modes.get(mode);
                    if (ModeCommand.isUserTypeMode(mode.charValue())) {
                        User user = new User(argument);
                        contactPanel.userModeChange(channel, user, mode.charValue(), command.isAddingModes());
                        if (command.isAddingModes()) msg = new MessageCommand(WebIRC.eventMessages.addUserMode(command.getSender().getNickname(), User.getModeName(mode.charValue()), user.getNickname()), MessageCommand.TYPE_ADDUSERMODE); else msg = new MessageCommand(WebIRC.eventMessages.removeUserMode(command.getSender().getNickname(), User.getModeName(mode.charValue()), user.getNickname()), MessageCommand.TYPE_REMOVEUSERMODE);
                    } else if (ModeCommand.isBanMode(mode.charValue())) {
                        if (command.isAddingModes()) msg = new MessageCommand(WebIRC.eventMessages.ban(command.getSender().getNickname(), argument), MessageCommand.TYPE_BAN); else msg = new MessageCommand(WebIRC.eventMessages.unban(command.getSender().getNickname(), argument), MessageCommand.TYPE_UNBAN);
                    } else msg = new MessageCommand(WebIRC.eventMessages.changeMode(command.getSender().getNickname(), command.getMode()), MessageCommand.TYPE_MODE);
                    messagePanel.addMessage(channel, msg);
                }
            } else {
            }
        }
