    public Channel getChannel(String name) {
        for (Iterator i = channels.iterator(); i.hasNext(); ) {
            Channel c = (Channel) i.next();
            if (name.equals(c.getName())) return c;
        }
        return null;
    }
