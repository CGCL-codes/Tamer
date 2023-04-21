    @Test
    public void testRequestRetransmitAll() {
        state.getChannels().put(ID, channel);
        state.getChannels().put(ID2, channel);
        mocks.checking(new Expectations() {

            {
                one(channel).requestRetransmitAll();
            }
        });
        candidate.requestRetransmitAll(ID);
    }
