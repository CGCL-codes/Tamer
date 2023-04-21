    private void fadeChannelValues(final CueValueSet startCue, final CueValueSet endCue) {
        connector.toggleDataEntry(false);
        new Thread() {

            public void run() {
                Channel[] startChannels = startCue.getAllChannels();
                Channel[] endChannels = endCue.getAllChannels();
                ArrayList<Channel> changedAddrs = new ArrayList<Channel>();
                ArrayList<Integer> changedSources = new ArrayList<Integer>();
                for (int i = 0; i < startChannels.length; i++) {
                    if (startChannels[i].value != endChannels[i].value) {
                        changedAddrs.add(startChannels[i]);
                        if (startChannels[i].value > endChannels[i].value) changedSources.add(ChannelValues.FADE_DOWN_SOURCE); else changedSources.add(ChannelValues.FADE_UP_SOURCE);
                    }
                }
                float[][] sources = new float[changedSources.size()][];
                for (int i = 0; i < changedSources.size(); i++) sources[i] = new float[] { changedSources.get(i) };
                connector.setChannelSources(changedAddrs.toArray(new Channel[0]), sources);
                ViewUpdater viewUpdater = new ViewUpdater(150, changedAddrs.toArray(new Channel[0]), endCue, startCue);
                endCue.setFadeLevel(0);
                endCue.setCombineMethod(IValueSet.ADD);
                channelValues.addSet(endCue);
                startCue.setCombineMethod(IValueSet.ADD);
                try {
                    dmxDevice.fadeValues(startCue, endCue, Channel.getAddresses(changedAddrs.toArray(new Channel[0])), new IChannelValueGetter() {

                        public short getChannelValue(short address) {
                            return channelValues.getChannelValue(address);
                        }
                    });
                } catch (IDMXDeviceException e) {
                    viewUpdater.cancel();
                    endCue.setCombineMethod(IValueSet.AVERAGE);
                    startCue.setCombineMethod(IValueSet.AVERAGE);
                    writeError(e);
                    return;
                }
                viewUpdater.cancel();
                endCue.setCombineMethod(IValueSet.AVERAGE);
                startCue.setCombineMethod(IValueSet.AVERAGE);
                connector.updateFadeUpProgress((int) (100 * endCue.getFadeLevel() + 0.5));
                connector.updateFadeDownProgress((int) (100 * startCue.getFadeLevel() + 0.5));
                channelValues.removeSet(startCue);
                connector.updateChannels(channelValues.getChannels(changedAddrs.toArray(new Channel[0])), channelValues.getChannelSources(changedAddrs.toArray(new Channel[0])));
                connector.toggleDataEntry(true);
            }
        }.start();
    }
