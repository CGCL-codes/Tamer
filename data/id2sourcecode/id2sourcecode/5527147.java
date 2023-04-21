    private static Video trim(Video video) {
        if (video != null) {
            video.setActors(Tools.trim(video.getActors(), 512));
            video.setAdvisories(Tools.trim(video.getAdvisories(), 255));
            video.setAudioCodec(Tools.trim(video.getAudioCodec(), 20));
            video.setBookmarks(Tools.trim(video.getBookmarks(), 255));
            video.setCallsign(Tools.trim(video.getCallsign(), 255));
            video.setChannel(Tools.trim(video.getChannel(), 255));
            video.setChoreographers(Tools.trim(video.getChoreographers(), 255));
            video.setColor(Tools.trim(video.getColor(), 20));
            video.setDescription(Tools.trim(video.getDescription(), 255));
            video.setDirectors(Tools.trim(video.getDirectors(), 255));
            video.setEpisodeTitle(Tools.trim(video.getEpisodeTitle(), 255));
            video.setExecProducers(Tools.trim(video.getExecProducers(), 255));
            video.setGuestStars(Tools.trim(video.getGuestStars(), 255));
            video.setHosts(Tools.trim(video.getHosts(), 255));
            video.setIcon(Tools.trim(video.getIcon(), 255));
            video.setMimeType(Tools.trim(video.getMimeType(), 50));
            video.setOrigen(Tools.trim(video.getOrigen(), 30));
            video.setPath(Tools.trim(video.getPath(), 1024));
            video.setProducers(Tools.trim(video.getProducers(), 255));
            video.setProgramGenre(Tools.trim(video.getProgramGenre(), 255));
            video.setRating(Tools.trim(video.getRating(), 255));
            video.setRecordingQuality(Tools.trim(video.getRecordingQuality(), 255));
            video.setSeriesGenre(Tools.trim(video.getSeriesGenre(), 255));
            video.setSeriesTitle(Tools.trim(video.getSeriesTitle(), 255));
            video.setShowType(Tools.trim(video.getShowType(), 255));
            video.setSource(Tools.trim(video.getSource(), 255));
            video.setStation(Tools.trim(video.getStation(), 255));
            video.setTitle(Tools.trim(video.getTitle(), 255));
            video.setTone(Tools.trim(video.getTone(), 50));
            video.setUploaded(Tools.trim(video.getUploaded(), 255));
            video.setUrl(Tools.trim(video.getUrl(), 1024));
            video.setVideoCodec(Tools.trim(video.getVideoCodec(), 20));
            video.setVideoResolution(Tools.trim(video.getVideoResolution(), 20));
            video.setWriters(Tools.trim(video.getWriters(), 255));
            video.setTivo(Tools.trim(video.getTivo(), 255));
        }
        return video;
    }
