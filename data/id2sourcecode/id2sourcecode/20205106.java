    @Override
    public void register(JSONRPCBridge bridge) {
        bridge.registerObject("loginFrontService", getLoginService());
        bridge.registerObject("formFrontService", getFormService());
        bridge.registerObject("commentFrontService", getCommentService());
        bridge.registerObject("searchFrontService", getSearchService());
        bridge.registerObject("channelApiFrontService", getChannelApiService());
        registerPluginServices(bridge);
    }
