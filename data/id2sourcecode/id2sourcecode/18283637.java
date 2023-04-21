    public void setData(BlueData data) {
        if (this.data != null) {
            Arrangement arrangement = this.data.getArrangement();
            arrangement.removeAutomatableCollectionListener(this);
            for (int i = 0; i < arrangement.size(); i++) {
                Instrument instr = arrangement.getInstrument(i);
                if (instr instanceof Automatable) {
                    Automatable temp = (Automatable) instr;
                    ParameterList parameters = temp.getParameterList();
                    parameters.removeParameterListListener(this);
                }
            }
            Mixer mixer = this.data.getMixer();
            ChannelList channels = mixer.getChannels();
            channels.removeChannelListListener(this);
            for (int i = 0; i < channels.size(); i++) {
                removeListenerFromChannel(channels.getChannel(i));
            }
            ChannelList subChannels = mixer.getSubChannels();
            subChannels.removeChannelListListener(this);
            for (int i = 0; i < subChannels.size(); i++) {
                removeListenerFromChannel(subChannels.getChannel(i));
            }
            removeListenerFromChannel(mixer.getMaster());
            if (this.pObj != null) {
                this.pObj.removePolyObjectListener(this);
            }
            this.data.removePropertyChangeListener(renderTimeListener);
        }
        allParameters.clear();
        if (data == null) {
            return;
        }
        Arrangement arrangement = data.getArrangement();
        for (int i = 0; i < arrangement.size(); i++) {
            Instrument instr = arrangement.getInstrument(i);
            if (instr instanceof Automatable) {
                Automatable temp = (Automatable) instr;
                ParameterList parameters = temp.getParameterList();
                allParameters.addAll(parameters.getParameters());
                parameters.addParameterListListener(this);
            }
        }
        arrangement.addAutomatableCollectionListener(this);
        Mixer mixer = data.getMixer();
        ChannelList channels = mixer.getChannels();
        channels.addChannelListListener(this);
        for (int i = 0; i < channels.size(); i++) {
            addListenerToChannel(channels.getChannel(i));
        }
        ChannelList subChannels = mixer.getSubChannels();
        subChannels.addChannelListListener(this);
        for (int i = 0; i < subChannels.size(); i++) {
            addListenerToChannel(subChannels.getChannel(i));
        }
        addListenerToChannel(mixer.getMaster());
        this.data = data;
        this.pObj = data.getPolyObject();
        this.pObj.addPolyObjectListener(this);
        this.data.addPropertyChangeListener(renderTimeListener);
    }
