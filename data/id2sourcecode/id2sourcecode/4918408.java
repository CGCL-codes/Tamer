    public void addChild(ChannelImpl channel) {
        ChannelId child = channel.getChannelId();
        if (!_id.isParentOf(child)) {
            throw new IllegalArgumentException(_id + " not parent of " + child);
        }
        String next = child.getSegment(_id.depth());
        if ((child.depth() - _id.depth()) == 1) {
            ChannelImpl old = _children.putIfAbsent(next, channel);
            if (old != null) throw new IllegalArgumentException("Already Exists");
            if (ChannelId.WILD.equals(next)) _wild = channel; else if (ChannelId.WILDWILD.equals(next)) _wildWild = channel;
        } else {
            ChannelImpl branch = _children.get(next);
            branch = (ChannelImpl) _bayeux.getChannel((_id.depth() == 0 ? "/" : (_id.toString() + "/")) + next, true);
            branch.addChild(channel);
        }
    }
