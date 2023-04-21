    @Test
    public void testRetransmitAll() {
        state.getChannels().put(ID, channel);
        state.getChannels().put(ID2, channel);
        mocks.checking(new Expectations() {

            {
                one(channel).retransmitAll();
            }
        });
        candidate.retransmitAll(ID);
    }
