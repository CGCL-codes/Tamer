    private void setAudioTrail(Object source, AudioTrail at) {
        this.at = at;
        if (!audioTracks.isEmpty()) throw new IllegalStateException("Cannot call repeatedly");
        final List collNewTracks = new ArrayList();
        final int numChannels = at.getChannelNum();
        final double deltaAngle = 360.0 / numChannels;
        final double startAngle = numChannels < 2 ? 0.0 : -deltaAngle / 2;
        AudioTrack t;
        audioTracks.setTrail(at);
        for (int ch = 0; ch < at.getChannelNum(); ch++) {
            t = new AudioTrack(audioTracks, ch);
            t.setName(String.valueOf(ch + 1));
            t.getMap().putValue(source, AudioTrack.MAP_KEY_PANAZIMUTH, new Double(startAngle + ch * deltaAngle));
            collNewTracks.add(t);
        }
        audioTracks.addAll(source, collNewTracks);
        tracks.addAll(source, collNewTracks);
        selectedTracks.addAll(source, collNewTracks);
        updateTitle();
    }
