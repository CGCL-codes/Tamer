    public static void clearCompilationVarNames(BlueData data) {
        Arrangement arrangement = data.getArrangement();
        for (int i = 0; i < arrangement.size(); i++) {
            Instrument instr = arrangement.getInstrument(i);
            if (instr instanceof Automatable) {
                Automatable temp = (Automatable) instr;
                temp.getParameterList().clearCompilationVarNames();
            }
        }
        Mixer mixer = data.getMixer();
        ChannelList channels = mixer.getChannels();
        for (int i = 0; i < channels.size(); i++) {
            clearChannelCompilationVar(channels.getChannel(i));
        }
        ChannelList subChannels = mixer.getSubChannels();
        for (int i = 0; i < subChannels.size(); i++) {
            clearChannelCompilationVar(subChannels.getChannel(i));
        }
        clearChannelCompilationVar(mixer.getMaster());
    }
