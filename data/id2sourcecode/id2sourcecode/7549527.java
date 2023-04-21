    public ChannelFilter(final int channel) {
        super(new Conditions.ShortMsgCondition() {

            public boolean canPass(ShortMessage message) {
                return message.getChannel() == channel;
            }
        });
    }
