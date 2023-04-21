    public void add(int index, Object element) {
        Message message = (Message) element;
        ChannelRef channel = new ChannelRef(message.getChannel());
        for (Iterator cit = this.treelist.iterator(); cit.hasNext(); ) {
            ChannelRef cref = (ChannelRef) cit.next();
            if (cref.getName().equals(message.getChannel())) {
                channel = cref;
                break;
            }
        }
        this.treelist.add(channel);
        AvatarRef avatar = (AvatarRef) channel.get(message.getAvatar());
        if (avatar == null) {
            avatar = new AvatarRef(message.getAvatar());
            channel.add(avatar);
        }
        MessageRef mref = new MessageRef(channel, avatar, message);
        if (!avatar.contains(mref)) {
            avatar.add(mref);
            this.flatlist.add(index, mref);
        } else System.out.println("duplicate:" + mref);
    }
