    @Test
    public void testUnsubscribeRemote() {
        mocks.checking(new Expectations() {

            {
                one(context).getIdentity();
                will(returnValue("local"));
                one(channel).removeListener(params, listener);
                will(returnValue(true));
                final List<IEventListener> listeners = CollectionFactory.newList();
                atLeast(1).of(channel).getListeners(params);
                will(returnValue(listeners));
                one(channel).unsubscribe(params);
                will(returnValue(true));
                final SubscriptionParameters subscriptionParameters = params;
                atMost(1).of(channel).getSubscribedSources(subscriptionParameters);
                will(returnValue(CollectionFactory.newList()));
            }
        });
        final DualValue<ISubscriptionParameters, IEventListener> values = new DualValue<ISubscriptionParameters, IEventListener>(params, listener);
        candidate.getState().getRemoteSubscriptions().get(ID).add(values);
        candidate.getState().getChannels().put(ID, channel);
        assertTrue(candidate.unsubscribe(ID, ID_REGEX, TYPE, DOMAIN, listener));
        assertFalse("values found", candidate.getState().getRemoteSubscriptions().get(ID).contains(values));
    }
