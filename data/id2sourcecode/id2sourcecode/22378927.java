    @Test
    public void testRequestRetransmit() {
        state.getChannels().put(ID, channel);
        state.getChannels().put(ID2, channel);
        mocks.checking(new Expectations() {

            {
                one(channel).requestRetransmit(ID_REGEX, TYPE, DOMAIN);
            }
        });
        candidate.requestRetransmit(ID, ID_REGEX, TYPE, DOMAIN);
    }
