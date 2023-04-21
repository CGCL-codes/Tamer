    @Test
    public void testRetransmitAllToAll() {
        state.getChannels().put(ID, channel);
        state.getChannels().put(ID2, channel);
        mocks.checking(new Expectations() {

            {
                exactly(2).of(channel).retransmit(ID_REGEX, TYPE, DOMAIN);
            }
        });
        candidate.retransmitToAll(ID_REGEX, TYPE, DOMAIN);
    }
