    static JoinEvent regularJoin(String data, Connection con) {
        Matcher m = joinPattern.matcher(data);
        if (m.matches()) {
            try {
                JoinEvent joinEvent = new JoinEventImpl(data, myManager.getSessionFor(con), m.group(1), m.group(2), m.group(3).toLowerCase(), con.getChannel(m.group(4).toLowerCase()).getName(), con.getChannel(m.group(4).toLowerCase()));
                return joinEvent;
            } catch (Exception e) {
                System.err.println(data);
                for (Channel chan : con.getChannels()) {
                    System.err.println(chan.getName());
                }
                e.printStackTrace();
            }
        }
        debug("JOIN_EVENT", data);
        return null;
    }
