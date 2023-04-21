    @Test
    public void testRetransmitToAll() {
        state.getChannels().put(ID, channel);
        state.getChannels().put(ID2, channel);
        mocks.checking(new Expectations() {

            {
                exactly(2).of(channel).retransmitAll();
            }
        });
        candidate.retransmitAllToAll();
    }
