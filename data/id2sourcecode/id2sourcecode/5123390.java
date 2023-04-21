    public void validate(ModelObject obj, ValidationContext context, ModelListener l) {
        ChannelList elem = (ChannelList) obj;
        for (int i = 0; i < elem.getChannels().size(); i++) {
            context.validate(elem.getChannels().get(i), l);
        }
    }
