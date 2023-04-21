    public static Sequence splitChannelsToMultiTrack(Sequence sequence) {
        System.out.println("Scanning sequence with " + sequence.getTracks().length + " tracks.");
        Track track = sequence.getTracks()[0];
        boolean[] channelsUsed = new boolean[16];
        for (int n = 0; n < track.size(); n++) {
            MidiEvent event = track.get(n);
            if (event.getMessage() instanceof ShortMessage) {
                ShortMessage message = (ShortMessage) event.getMessage();
                channelsUsed[message.getChannel()] = true;
            }
        }
        System.out.print("Channels used: ");
        for (int n = 0; n < channelsUsed.length; n++) {
            if (channelsUsed[n]) System.out.print(n + " ");
        }
        System.out.println();
        Integer[] channelToTrackMapping = new Integer[16];
        int tracksCreated = 0;
        for (int n = 0; n < channelsUsed.length; n++) {
            if (channelsUsed[n]) {
                sequence.createTrack();
                channelToTrackMapping[n] = tracksCreated++;
            }
        }
        System.out.println("Created " + tracksCreated + " new tracks.");
        for (int n = 0; n < track.size(); n++) {
            MidiEvent event = track.get(n);
            if (event.getMessage() instanceof ShortMessage) {
                ShortMessage message = (ShortMessage) event.getMessage();
                sequence.getTracks()[channelToTrackMapping[message.getChannel()] + 1].add(event);
                track.remove(event);
                n--;
            }
        }
        System.out.println("Events moved into new tracks. Initial track kept as mastertrack for tempo change etc.");
        return sequence;
    }
