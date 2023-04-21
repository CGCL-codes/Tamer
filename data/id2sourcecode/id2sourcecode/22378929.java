    @Test
    public void testRetransmit() {
        state.getChannels().put(ID, channel);
        state.getChannels().put(ID2, channel);
        mocks.checking(new Expectations() {

            {
                one(channel).retransmit(ID_REGEX, TYPE, DOMAIN);
            }
        });
        candidate.retransmit(ID, ID_REGEX, TYPE, DOMAIN);
    }
