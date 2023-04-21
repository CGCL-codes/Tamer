    public Channel getChannel(String name) {
        for (Iterator i = this.channels.iterator(); i.hasNext(); ) {
            Channel c = (Channel) i.next();
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }
