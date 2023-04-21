    public void execute(CommandMessage message) {
        Client client = (Client) message.getSource();
        Channel channel = client.getChannel();
        if (message.getParameterCount() == 0) {
            Locale locale = client.getUser().getLocale();
            for (int i = 0; i < modes.length; i++) {
                Message tmode = new PlineMessage("<red>/" + getAlias() + " <aqua>" + i + "</aqua> : <darkBlue>" + Language.getText("command.mode.message" + i, locale));
                client.send(tmode);
            }
        } else {
            int param = -1;
            try {
                param = Integer.parseInt(message.getParameter(0));
            } catch (NumberFormatException e) {
            }
            if (param >= 0 && param < modes.length) {
                updateSetting(channel.getConfig().getSettings(), modes[param]);
                PlineMessage enabled = new PlineMessage();
                enabled.setKey("command.mode.enabled", "key:command.mode.message" + param);
                channel.send(enabled);
            } else {
                PlineMessage error = new PlineMessage();
                error.setText("<red>/" + getAlias() + "</red> <blue><0-" + (modes.length - 1) + "></blue>");
                client.send(error);
            }
        }
    }
