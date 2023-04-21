    public static ArrayList getChannels(IRCMessage msg) {
        ArrayList list = new ArrayList(1);
        if (msg.getArgs().size() == 0) return list;
        String reply = (String) msg.getArgs().elementAt(1);
        if (reply.equals("")) return list;
        StringTokenizer t = new StringTokenizer(reply, " ", false);
        while (t.hasMoreTokens()) list.add(t.nextToken());
        return list;
    }
