    private void initialState() {
        logger.Debug("[Initial state] About to process");
        try {
            String episode = currentMediaFile.getEpisode();
            String title = currentMediaFile.getTitle();
            long start = currentMediaFile.getStartTime();
            long end = currentMediaFile.getEndTime();
            String duration = currentMediaFile.getDurationStr();
            String genre = currentMediaFile.getGenre();
            String year = currentMediaFile.getYear();
            if (start == end) {
                API sageApi = this.context == null ? API.apiLocalUI : new API(this.context);
                start = sageApi.mediaPlayerAPI.GetAvailableSeekingStart() / 1000;
                end = sageApi.mediaPlayerAPI.GetAvailableSeekingEnd() / 1000;
                duration = String.valueOf(end - start);
            }
            send(new Message(MessageType.CURRENT_START_TIME, String.valueOf(start)));
            send(new Message(MessageType.CURRENT_END_TIME, String.valueOf(end)));
            if (currentMediaFile.isMusicFile()) {
                AlbumAPI.Album album = currentMediaFile.getAlbum();
                String artist = currentMediaFile.getShow().GetPeopleInShowInRoles(new String[] { "Artist", "Artiste" });
                String albumName = album.GetAlbumName();
                String category = (genre.isEmpty() ? album.GetAlbumGenre() : genre);
                if (!albumName.isEmpty()) title = albumName + " - " + title;
                send(new Message(MessageType.CURRENT_ARTIST, (artist.isEmpty() ? album.GetAlbumArtist() : artist)));
                send(new Message(MessageType.CURRENT_CATEGORY, category));
                send(new Message(MessageType.CURRENT_TYPE, "Audio"));
            } else {
                String actors = currentMediaFile.getShow().GetPeopleInShowInRoles(new String[] { "Actor", "Acteur", "Guest", "Invité", "Special guest", "Invité spécial" });
                String channel = currentMediaFile.getChannel();
                if (!channel.isEmpty()) {
                    send(new Message(MessageType.CURRENT_CHANNEL, channel));
                    send(new Message(MessageType.CURRENT_STATION_NAME, channel + (currentMediaFile.getAiring().GetChannel() != null ? " " + currentMediaFile.getAiring().GetChannel().GetChannelDescription() : "")));
                }
                send(new Message(MessageType.CURRENT_CATEGORY, genre));
                send(new Message(MessageType.CURRENT_ACTORS, actors));
                if (currentMediaFile.isDvd()) send(new Message(MessageType.CURRENT_TYPE, "DVD")); else if (currentMediaFile.getIsLive()) send(new Message(MessageType.CURRENT_TYPE, "Live TV")); else if (currentMediaFile.getIsTvFile()) send(new Message(MessageType.CURRENT_TYPE, "TV")); else send(new Message(MessageType.CURRENT_TYPE, "Video"));
            }
            send(new Message(MessageType.CURRENT_TITLE, title));
            send(new Message(MessageType.CURRENT_DURATION, duration));
            send(new Message(MessageType.CURRENT_DESC, currentMediaFile.getDescription()));
            send(new Message(MessageType.CURRENT_YEAR, (year.isEmpty() ? "0" : year)));
            if (!episode.equalsIgnoreCase(title) || currentMediaFile.isMusicFile()) send(new Message(MessageType.CURRENT_EPISODE, episode));
            send(new Message(MessageType.CURRENT_ID, String.valueOf(currentMediaFile.getMediaFileId())));
        } catch (Exception ex) {
            logger.Debug(ex.getMessage());
        }
    }
