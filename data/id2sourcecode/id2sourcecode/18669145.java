    public static ArrayList getChannels(IRCMessage msg) {
        StringTokenizer t = new StringTokenizer((String) msg.getArgs().elementAt(0), ",", false);
        ArrayList list = new ArrayList(1);
        while (t.hasMoreTokens()) list.add(t.nextToken());
        return list;
    }
