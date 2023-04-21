    static NickListEvent nickList(String data, Connection con) {
        Matcher m = nickListPattern.matcher(data);
        if (m.matches()) {
            NickListEvent nle = new NickListEventImpl(data, myManager.getSessionFor(con), con.getChannel(m.group(1).toLowerCase()), con.getChannel(m.group(1).toLowerCase()).getNicks());
            return nle;
        }
        debug("NICK_LIST", data);
        return null;
    }
