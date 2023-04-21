    public ModelObject project(ProjectorContext context, ModelObject model, Role role, ModelListener l) {
        ChannelList ret = new ChannelList();
        ChannelList source = (ChannelList) model;
        for (int i = 0; i < source.getChannels().size(); i++) {
            if (source.getChannels().get(i).equals(role) == false) {
                Channel ch = new Channel();
                ch.setName(source.getChannels().get(i).getName());
                context.setState(ch.getName(), ch);
                ret.getChannels().add(ch);
            }
        }
        return (ret);
    }
