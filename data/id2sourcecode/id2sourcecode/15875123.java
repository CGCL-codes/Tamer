    public void fadeChannelValues(final CueValueSet cue) {
        connector.toggleDataEntry(false);
        new Thread() {

            public void run() {
                Channel[] startChannels = channelValues.getAllChannels();
                Channel[] endChannels = cue.getAllChannels();
                ArrayList<Channel> changedAddrs = new ArrayList<Channel>();
                ArrayList<Integer> changedSources = new ArrayList<Integer>();
                for (int i = 0; i < startChannels.length; i++) {
                    Channel c1 = startChannels[i];
                    Channel c2 = endChannels[i];
                    if (startChannels[i].value != endChannels[i].value) {
                        changedAddrs.add(startChannels[i]);
                        if (startChannels[i].value > endChannels[i].value) changedSources.add(ChannelValues.FADE_DOWN_SOURCE); else changedSources.add(ChannelValues.FADE_UP_SOURCE);
                    }
                }
                float[][] sources = new float[changedSources.size()][];
                for (int i = 0; i < changedSources.size(); i++) sources[i] = new float[] { changedSources.get(i) };
                connector.setChannelSources(changedAddrs.toArray(new Channel[0]), sources);
                ViewUpdater viewUpdater = new ViewUpdater(150, changedAddrs.toArray(new Channel[0]), cue);
                cue.setFadeLevel(0);
                channelValues.addSet(cue);
                try {
                    dmxDevice.fadeValues(cue, Channel.getAddresses(changedAddrs.toArray(new Channel[0])), new IChannelValueGetter() {

                        public short getChannelValue(short address) {
                            return channelValues.getChannelValue(address);
                        }
                    });
                } catch (IDMXDeviceException e) {
                    viewUpdater.cancel();
                    writeError(e);
                    return;
                }
                viewUpdater.cancel();
                connector.updateFadeUpProgress((int) (100 * cue.getFadeLevel() + 0.5));
                connector.updateChannels(channelValues.getChannels(changedAddrs.toArray(new Channel[0])), channelValues.getChannelSources(changedAddrs.toArray(new Channel[0])));
                connector.toggleDataEntry(true);
            }
        }.start();
    }
