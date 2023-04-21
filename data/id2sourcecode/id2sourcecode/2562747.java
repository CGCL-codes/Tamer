    public static ArrayList getChannels(IRCMessage msg) {
        ArrayList list = new ArrayList(1);
        try {
            StringTokenizer t = new StringTokenizer((String) msg.getArgs().elementAt(0), ",", false);
            while (t.hasMoreTokens()) list.add(t.nextToken());
            return list;
        } catch (ArrayIndexOutOfBoundsException e) {
            return list;
        }
    }
