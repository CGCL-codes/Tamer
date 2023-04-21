    @Override
    public void validate(final ValidationSupport support) {
        new ChannelCountValidator().validate(support, parameters.getChannelCount());
        new SubmasterCountValidator().validate(support, parameters.getSubmasterCount());
        new CueCountValidator().validate(support, parameters.getCueCount());
    }
