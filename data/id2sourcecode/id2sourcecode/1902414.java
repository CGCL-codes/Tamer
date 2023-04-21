    public static CueSheet convertMixmeisterPlaylistToCueSheet(MixmeisterPlaylist mmp, boolean readID3) {
        final CueSheet cueSheet = new CueSheet("", "");
        double position = 0;
        int trackNo = 1;
        final List<Track> tracks = mmp.getTracks();
        if (tracks.isEmpty()) {
            return cueSheet;
        }
        final Comparator<Marker> markerPositionComparator = new MarkerPositionComparator();
        double previousBpmMarkerPosition = 0;
        double previousBpm = tracks.iterator().next().getHeader().getBpm();
        for (int i = 0; i < mmp.getTracks().size(); ++i) {
            mixmeister.mmp.Track mmpTrack = mmp.getTracks().get(i);
            switch(mmpTrack.getHeader().getTrackType()) {
                case TrackType.OVERLAY:
                case TrackType.OVERLAY_WITH_BEATSYNC:
                case TrackType.OVERLAY_WITHOUT_BEATSYNC:
                    break;
                default:
                    cue.Track cueTrack = new cue.Track(trackNo++);
                    cueTrack.setPerformer("Unknown artist");
                    cueTrack.setTitle("Track " + cueTrack.getNumber());
                    if (readID3) {
                        applyMetadataFromID3(cueTrack, new File(mmpTrack.getFileName()));
                    }
                    cueTrack.getIndices().add(new Index(position));
                    cueSheet.getTracks().add(cueTrack);
                    Marker outroAnchor = new LinkedList<Marker>(mmpTrack.getMarkers(Marker.OUTRO_RANGE)).getLast();
                    Marker introAnchor = new LinkedList<Marker>(mmpTrack.getMarkers(Marker.INTRO_RANGE)).getLast();
                    double trackLength = (outroAnchor.getPosition() - introAnchor.getPosition()) / 1000000.0;
                    double trackBpm = mmpTrack.getHeader().getBpm();
                    Set<Marker> tempoMarkers = new TreeSet<Marker>(markerPositionComparator);
                    tempoMarkers.addAll(mmpTrack.getMarkers(Marker.INTRO_TEMPO_MARKER));
                    tempoMarkers.addAll(mmpTrack.getMarkers(Marker.TEMPO_MARKER));
                    for (Marker tempoMarker : tempoMarkers) {
                        double markerPosition = tempoMarker.getPosition() / 1000000.0;
                        double bpm = Float.intBitsToFloat(tempoMarker.getVolume());
                        double timeElapsed = position + markerPosition - previousBpmMarkerPosition;
                        double averageBpm = (bpm + previousBpm) / 2;
                        double bpmModifier = trackBpm / averageBpm;
                        trackLength -= timeElapsed - (timeElapsed * bpmModifier);
                        previousBpmMarkerPosition = markerPosition;
                        previousBpm = bpm;
                    }
                    position += trackLength;
                    break;
            }
        }
        return cueSheet;
    }
