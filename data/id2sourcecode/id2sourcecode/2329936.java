    public static ArrayList<String> getChannels(IRCMessage msg) {
        ArrayList<String> list = new ArrayList<String>(20);
        try {
            StringTokenizer t = new StringTokenizer(msg.getArgs().get(0), ",", false);
            while (t.hasMoreTokens()) list.add(t.nextToken());
            return list;
        } catch (IndexOutOfBoundsException e) {
            return list;
        }
    }
