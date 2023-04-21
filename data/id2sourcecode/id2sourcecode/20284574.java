    public static ArrayList<String> getChannels(IRCMessage msg) {
        ArrayList<String> list = new ArrayList<String>(20);
        if (msg.getArgs().size() == 0) return list;
        String reply = msg.getArgs().get(1);
        if (reply.equals("")) return list;
        StringTokenizer t = new StringTokenizer(reply, " ", false);
        while (t.hasMoreTokens()) list.add(t.nextToken());
        return list;
    }
