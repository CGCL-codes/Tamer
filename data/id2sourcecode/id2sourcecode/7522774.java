    public final boolean process(final String line, final Session session) {
        StringTokenizer token = new StringTokenizer(line);
        if (!token.hasMoreTokens()) {
            return false;
        }
        String channel = token.nextToken();
        if (!isKnown(channel)) {
            return false;
        }
        if (!token.hasMoreTokens()) {
            return false;
        }
        String parameter = token.nextToken();
        if (!token.hasMoreTokens()) {
            Channel targetChannel = mChannels.get(channel);
            SubCommand command = mCommands.get(parameter);
            if (null != command) {
                command.process(targetChannel, session);
                return true;
            }
        }
        String message = line.substring(channel.length() + 1);
        channelSay(channel, message, session);
        return true;
    }
