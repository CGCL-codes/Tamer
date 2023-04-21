    public String getChannelList() {
        String ret = "<ret><conferencelist>";
        for (int i = 0; i < sessions.size(); i++) {
            ChatSession s1 = (ChatSession) sessions.elementAt(i);
            ret += "<channel><roomid>" + s1.name + "</roomid><topic></topic><usercount>" + s1.getUserCount() + "</usercount>";
            if (s1.priv) ret += "<private/>";
            ret += "</channel>";
        }
        ret += "</conferencelist></ret>";
        return ret;
    }
