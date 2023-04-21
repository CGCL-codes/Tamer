    public void processingStarted(final XiMessage xiMessage, final XiChannel xiChannel) {
        final Message sapMessage = ((XiMessageSapImpl) xiMessage).getSapMessage();
        this.mm = MonitoringManagerFactory.getInstance().getMonitoringManager();
        this.ps = ProcessContextFactory.getParamSet().message(sapMessage).channel(((XiChannelSapImpl) xiChannel).getChannel());
        if (this.ps != null) this.pc = ProcessContextFactory.getInstance().createProcessContext(this.ps);
        reportOk("Message processing started.");
    }
