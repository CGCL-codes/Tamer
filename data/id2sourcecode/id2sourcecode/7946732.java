    @Override
    public Message getMessage(String line) {
        if (line == null || line.equals("")) {
            return null;
        }
        Logger.debug(this, "Received from server: " + line);
        String textLine = ":(.*?)!.*? PRIVMSG " + getChannels() + " :(.*)";
        String indirectLine = ":(.*?)!.*? PRIVMSG " + getChannels() + " :\001ACTION (.*?)\001";
        String nickLine = ":(.*?)!.*? NICK :(.*)";
        String joinLine = ":(.*?)!.*? JOIN " + getChannels();
        String partLine = ":(.*?)!.*? PART " + getChannels();
        String queryLine = ":(.*?)!.*? PRIVMSG " + getName() + " :(.*)";
        String quitLine = ":(.*?)!.*? QUIT :(.*)";
        if (line.toUpperCase().startsWith("PING")) {
            socketOut.println("PONG" + line.substring("PING".length()));
            return new PingMessage();
        } else if (line.matches(textLine)) {
            lastChannel = line.replaceAll(textLine, "$2");
            return new TextMessage(line.replaceAll(textLine, "$1"), line.replaceAll(textLine, "$3"));
        } else if (line.matches(indirectLine)) {
            lastChannel = line.replaceAll(indirectLine, "$2");
            return new IndirectMessage(line.replaceAll(indirectLine, "$1"), line.replaceAll(indirectLine, "$3"));
        } else if (line.matches(nickLine)) {
            users.remove(line.replaceAll(nickLine, "$1"));
            users.add(line.replaceAll(nickLine, "$2"));
            return new NickchangeMessage(line.replaceAll(nickLine, "$1"), line.replaceAll(nickLine, "$2"));
        } else if (line.matches(joinLine)) {
            lastChannel = line.replaceAll(joinLine, "$2");
            users.add(line.replaceAll(joinLine, "$1"));
            return new LoginMessage(line.replaceAll(joinLine, "$1"));
        } else if (line.matches(partLine)) {
            lastChannel = line.replaceAll(partLine, "$2");
            users.remove(line.replaceAll(partLine, "$1"));
            return new LogoutMessage(line.replaceAll(partLine, "$1"), "");
        } else if (line.matches(queryLine)) {
            return new QueryMessage(line.replaceAll(queryLine, "$1"), line.replaceAll(queryLine, "$2"));
        } else if (line.matches(quitLine)) {
            users.remove(line.replaceAll(quitLine, "$1"));
            return new LogoutMessage(line.replaceAll(quitLine, "$1"), line.replaceAll(quitLine, "$2"));
        } else {
            return new RawMessage(line);
        }
    }
