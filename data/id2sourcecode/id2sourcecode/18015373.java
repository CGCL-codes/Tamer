    public void removeUserFromChannel(String channel, String user) {
        Channel c = getChannel(channel);
        if (c != null) {
            c.removeMember(user);
            if (c.numMembers() == 0 && !c.isPermanent()) {
                removeChannel(c);
            }
        }
        Vector v = (Vector) users.get(user);
        if (v != null) {
            v.removeElement(channel);
        }
    }
