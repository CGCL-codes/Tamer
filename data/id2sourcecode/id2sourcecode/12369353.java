    @Test
    public void testConnectionDestroyedEventHandler_ReconnectRequired() {
        mocks.checking(new Expectations() {

            {
                allowing(connectionParams).getRemoteContextIdentity();
                will(returnValue(ID));
                one(context).getSystemEventSource(ConnectionDestroyedEvent.class);
                will(returnValue(sysEventSource));
                one(context).getConnectionDiscoverer();
                will(returnValue(connectionDiscoverer));
                one(connectionDiscoverer).connectionDestroyed(ID);
            }
        });
        ConnectionDestroyedEvent event = new ConnectionDestroyedEvent(context, ID);
        ConnectionDestroyedEventHandler candidate = new ConnectionDestroyedEventHandler(state);
        candidate.getState().getChannels().put(ID, channel);
        candidate.getState().getDiscoveredContexts().put(ID, connectionParams);
        candidate.getState().getRemoteSubscriptions().get(ID).add(new DualValue<ISubscriptionParameters, IEventListener>(null, null));
        mocks.checking(new Expectations() {

            {
                one(channel).destroy();
            }
        });
        candidate.getState().getConnectedContexts().adjustCount(ID, 1);
        candidate.update(event);
        assertEquals("connection count", 0, candidate.getState().getConnectedContexts().getCount(ID));
    }
