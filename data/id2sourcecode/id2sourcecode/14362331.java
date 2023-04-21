    public Queue(QueueAdmissionsController anAdmissionsController) {
        this(anAdmissionsController, ChannelFactory.instance().getChannel());
    }
