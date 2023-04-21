    private static void clearUnusedMixerChannels(Mixer mixer, Arrangement arrangement) {
        ArrayList ids = new ArrayList();
        for (int i = 0; i < arrangement.getRowCount(); i++) {
            InstrumentAssignment ia = arrangement.getInstrumentAssignment(i);
            ids.add(ia.arrangementId);
        }
        ChannelList channelList = mixer.getChannels();
        channelList.clearChannelsNotInList(ids);
    }
