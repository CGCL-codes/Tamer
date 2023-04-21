    public static ArrayList getAllParameters(Arrangement arr, Mixer mixer) {
        ArrayList params = new ArrayList();
        for (int i = 0; i < arr.size(); i++) {
            InstrumentAssignment ia = arr.getInstrumentAssignment(i);
            if (ia.enabled) {
                Instrument instr = ia.instr;
                if (instr instanceof Automatable) {
                    Automatable auto = (Automatable) instr;
                    ParameterList list = auto.getParameterList();
                    params.addAll(list.getParameters());
                }
            }
        }
        if (mixer != null && mixer.isEnabled()) {
            ChannelList channels = mixer.getChannels();
            for (int i = 0; i < channels.size(); i++) {
                Channel channel = channels.getChannel(i);
                appendAllParametersFromChannel(params, channels.getChannel(i));
            }
            ChannelList subChannels = mixer.getSubChannels();
            for (int i = 0; i < subChannels.size(); i++) {
                appendAllParametersFromChannel(params, subChannels.getChannel(i));
            }
            appendAllParametersFromChannel(params, mixer.getMaster());
        }
        return params;
    }
