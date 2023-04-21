    static KickEvent kick(String data, Connection con) {
        System.out.println("IN KICK()");
        Matcher m = kickPattern.matcher(data);
        if (m.matches()) {
            System.out.println("CONNECTION: " + con);
            System.out.println("myManager: " + myManager);
            String channelName = m.group(4).toLowerCase();
            Channel c = con.getChannel(channelName);
            Session session = myManager.getSessionFor(con);
            System.out.println("SESSION RETRIEVED: " + session);
            KickEvent ke = new KickEventImpl(data, session, m.group(1), m.group(2), m.group(3), m.group(5), m.group(6), c);
            log.severe("BUILT EVENT: " + ke.toString());
            return ke;
        }
        debug("KICK", data);
        return null;
    }
