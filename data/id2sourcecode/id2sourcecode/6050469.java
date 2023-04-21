    public void commandPerformed(CommandEvent e) {
        String command = e.getCommand();
        if (e.getType() instanceof BotInfoType) {
            int first = command.indexOf("\"") + 1;
            int last = command.lastIndexOf("\"");
            String content = command.substring(first, last);
            if (checking) {
                if (content.equals("Users in channel " + users.getChannel() + ":")) {
                    starting = true;
                }
            }
        }
    }
