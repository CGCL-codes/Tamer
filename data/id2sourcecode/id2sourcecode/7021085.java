    protected ChannelSetAttribute(final String id, final int bufferId, final ChannelAttribute[] attributes) {
        super(id, "*XXee#".length() + (6 * attributes.length));
        this.bufferId = bufferId;
        this.attributes = attributes;
        Util.validate("attributesCount", attributes.length, 1, MAX_ATTRIBUTES);
        set2(3, bufferId);
        int offset = 5;
        for (ChannelAttribute attribute : attributes) {
            int channelId = attribute.getChannelId();
            int value = attribute.isEnabled() ? 0xFF : 0;
            set4(offset, channelId);
            set2(offset + 4, value);
            offset += 6;
        }
    }
