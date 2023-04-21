    @Override
    public void doTag(final XMLOutput output) throws Exception {
        if ((myName == null) || (myName.length() == 0)) {
            throw new MissingAttributeException("name");
        }
        Channel channel;
        if (myProvider != null) {
            channel = myProvider.getChannel(myName);
        } else {
            channel = BonsaiTagUtils.findChannelInParentTags(myName, this);
        }
        channel.setValue(myValue);
        invokeBody(output);
    }
