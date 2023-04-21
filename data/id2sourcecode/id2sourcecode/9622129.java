    static MessageEvent privateMsg(String data, Connection con, String channelPrefixRegex) {
        if (data.matches("^:\\S+\\s+PRIVMSG\\s+\\S+\\s+:.*$")) {
            Matcher m = privmsgPattern.matcher(data);
            m.matches();
            String target = m.group(4);
            return new MessageEventImpl(target.matches("^" + channelPrefixRegex + "{1}.+") ? con.getChannel(target.toLowerCase()) : null, m.group(3), m.group(5), m.group(1), data, myManager.getSessionFor(con), target.matches("^" + channelPrefixRegex + "{1}.+") ? Type.CHANNEL_MESSAGE : Type.PRIVATE_MESSAGE, m.group(2));
        }
        debug("MESSAGE", data);
        return null;
    }
