    public static ArrayList getActiveParameters(Arrangement arr, Mixer mixer) {
        ArrayList params = new ArrayList();
        for (int i = 0; i < arr.size(); i++) {
            InstrumentAssignment ia = arr.getInstrumentAssignment(i);
            if (ia.enabled) {
                Instrument instr = ia.instr;
                if (instr instanceof Automatable) {
                    Automatable auto = (Automatable) instr;
                    ParameterList list = auto.getParameterList();
                    addActiveParametersFromList(params, list);
                }
            }
        }
        if (mixer != null && mixer.isEnabled()) {
            ChannelList channels = mixer.getChannels();
            for (int i = 0; i < channels.size(); i++) {
                appendParametersFromChannel(params, channels.getChannel(i));
            }
            ChannelList subChannels = mixer.getSubChannels();
            for (int i = 0; i < subChannels.size(); i++) {
                appendParametersFromChannel(params, subChannels.getChannel(i));
            }
            appendParametersFromChannel(params, mixer.getMaster());
        }
        return params;
    }
