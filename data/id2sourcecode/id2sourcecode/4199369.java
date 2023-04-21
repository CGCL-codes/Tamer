    public static boolean checkSyncedAudio(List tis, boolean changesTimeline, ProcessingThread context, Flag hasSelectedAudio) {
        Track.Info ti;
        hasSelectedAudio.set(false);
        for (int i = 0; i < tis.size(); i++) {
            ti = (Track.Info) tis.get(i);
            if (changesTimeline && !ti.getChannelSync()) {
                if (context != null) context.setException(new IllegalStateException(AbstractApplication.getApplication().getResourceString("errAudioWillLooseSync")));
                return false;
            }
            if ((ti.trail instanceof AudioTrail) && ti.selected) {
                hasSelectedAudio.set(true);
            }
        }
        return true;
    }
