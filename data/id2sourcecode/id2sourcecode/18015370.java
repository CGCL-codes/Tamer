    public void addUserToChannel(String channel, String user) {
        Channel c = getChannel(channel);
        if (c == null) {
            c = new Channel();
            addChannel(c);
            c.setName(channel);
            c.setTopic("Welcome to the " + channel + " channel");
        }
        c.addMember(user);
        Vector v = (Vector) users.get(user);
        if (v == null) {
            v = new Vector();
            users.put(user, v);
        }
        v.addElement(channel);
    }
