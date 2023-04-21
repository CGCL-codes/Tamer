    private void mapChannels() {
        if (synth != null) {
            ArrayList<Track> tracks = trackCollection.getTracks();
            channelMapping = new HashMap<Instrument, Integer>();
            mappedChannels = 0;
            for (int i = 0; i < tracks.size(); i++) {
                Track track = tracks.get(i);
                SimpleInstrument instrument = track.getInstrument();
                Integer channel = channelMapping.get(instrument.instrument);
                if (channel == null) {
                    if (instrument.instrument != null && instrument.instrument.toString().contains("Drumkit")) {
                        channelMapping.put(instrument.instrument, 9);
                        synth.getChannels()[9].programChange(instrument.instrument.getPatch().getBank(), instrument.instrument.getPatch().getProgram());
                    } else if (instrument.instrument == null) {
                        trackCollection.removeListener(this);
                        track.setInstrument(new SimpleInstrument(defaultInstrument, DrumkitHelp.getPitch("High Tom 1")));
                        instrument = track.getInstrument();
                        channelMapping.put(instrument.instrument, 9);
                        synth.getChannels()[9].programChange(instrument.instrument.getPatch().getBank(), instrument.instrument.getPatch().getProgram());
                        trackCollection.addListener(this);
                    } else {
                        int channelNumber = mappedChannels;
                        if (channelNumber == 9) {
                            channelNumber++;
                        }
                        channelMapping.put(instrument.instrument, channelNumber);
                        mappedChannels++;
                        synth.getChannels()[channelNumber].programChange(instrument.instrument.getPatch().getBank(), instrument.instrument.getPatch().getProgram());
                    }
                }
            }
        }
    }
