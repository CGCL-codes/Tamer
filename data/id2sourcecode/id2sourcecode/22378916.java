    private void prepareForStart() {
        mocks.checking(new Expectations() {

            {
                one(state).init();
                one(state).start();
                allowing(state).getContext();
                will(returnValue(context));
                allowing(state).getEventHandler();
                will(returnValue(listener));
                allowing(state).getFrameReader();
                will(returnValue(frameReader));
                allowing(state).getFrameWriter();
                will(returnValue(frameWriter));
                allowing(state).getSubscriptionManager();
                will(returnValue(subscriptionManager));
                allowing(state).getRemoteSubscriptions();
                will(returnValue(remoteSubscriptions));
                allowing(state).getDiscoveredContexts();
                will(returnValue(discoveredConnections));
                allowing(state).getChannels();
                will(returnValue(channels));
            }
        });
    }
