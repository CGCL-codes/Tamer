    public void print() {
        System.out.println("Agent Name: " + name);
        for (String tmpTrackUID : trackMap.keySet()) {
            System.out.println("\tTrack name: " + trackMap.get(tmpTrackUID).getName());
            System.out.println("\tTrack ID: " + tmpTrackUID);
            System.out.println("\tTrack channel: " + (trackMap.get(tmpTrackUID).getChannel() + 1));
        }
    }
