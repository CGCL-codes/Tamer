    @Test
    public void testSubscribeRemote_ChannelExists() {
        mocks.checking(new Expectations() {

            {
                one(context).getIdentity();
                will(returnValue("local"));
                one(context).getSystemEventSource(RemoteContainerSubscriptionEvent.class);
                will(returnValue(sysEventSource));
                one(context).queueEvent(with(a(RemoteContainerSubscriptionEvent.class)));
            }
        });
        final DualValue<ISubscriptionParameters, IEventListener> values = new DualValue<ISubscriptionParameters, IEventListener>(params, listener);
        candidate.getState().getChannels().put(ID, channel);
        assertTrue(candidate.subscribe(ID, ID_REGEX, TYPE, DOMAIN, listener));
        assertTrue("values not found", candidate.getState().getRemoteSubscriptions().get(ID).contains(values));
    }
