    @Override
    protected void initAttributes() throws ConnectionException {
        super.initAttributes();
        getTimerAttributeModel().addStringScalarListener(timerListener);
        getMonitorAttributeModel().addStringScalarListener(monitorListener);
        getIntegrationTimeAttributeModel().addNumberScalarListener(iTimeListener);
        getIntegrationCountAttributeModel().addNumberScalarListener(iCountListener);
        getCountersAttributeModel().addListener(counterListListener);
        getZeroDExpChannelsAttributeModel().addListener(zerodListListener);
        getPseudoCountersAttributeModel().addListener(pcListListener);
        getOneDExpChannelsAttributeModel().addListener(onedListListener);
        getTwoDExpChannelsAttributeModel().addListener(twodListListener);
        getChannelsAttributeModel().addListener(channelsListListener);
    }
