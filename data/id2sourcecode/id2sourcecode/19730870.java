    public static List getInfos(List selectedTracks, List allTracks) {
        Track track;
        Trail trail;
        Track.Info ti;
        int chan;
        final Map mapInfos = new HashMap();
        final List collInfos = new ArrayList();
        for (int i = 0; i < allTracks.size(); i++) {
            track = (Track) allTracks.get(i);
            trail = track.getTrail();
            ti = (Track.Info) mapInfos.get(trail.getClass());
            if (ti == null) {
                ti = new Info(trail);
                mapInfos.put(ti.trail.getClass(), ti);
                collInfos.add(ti);
            }
            if (track instanceof AudioTrack) {
                chan = ((AudioTrack) track).getChannelIndex();
            } else {
                chan = 0;
            }
            if (selectedTracks.contains(track)) {
                ti.selected = true;
                ti.trackMap[chan] = true;
                ti.numTracks++;
            }
        }
        return collInfos;
    }
