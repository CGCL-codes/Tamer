    private void changeStates(States newState, MediaFile mf, MediaPlayerAPI sageApiMediaPlayer) {
        logger.Debug("[ChangeStates] About to process");
        try {
            currentMediaFile = new MediaStore(mf, sageApiMediaPlayer.IsCurrentMediaFileRecording());
            for (SagePlayer player : allPlayers) player.currentMediaFile = currentMediaFile;
            String episode = currentMediaFile.getEpisode();
            String title = currentMediaFile.getTitle();
            long start = currentMediaFile.getStartTime();
            long end = currentMediaFile.getEndTime();
            String duration = currentMediaFile.getDurationStr();
            String genre = currentMediaFile.getGenre();
            String year = currentMediaFile.getYear();
            if (start == end) {
                start = sageApiMediaPlayer.GetAvailableSeekingStart() / 1000;
                end = sageApiMediaPlayer.GetAvailableSeekingEnd() / 1000;
                duration = String.valueOf(end - start);
            }
            sendMessage(new Message(MessageType.CURRENT_START_TIME, String.valueOf(start)));
            sendMessage(new Message(MessageType.CURRENT_END_TIME, String.valueOf(end)));
            if (currentMediaFile.isMusicFile()) {
                AlbumAPI.Album album = currentMediaFile.getAlbum();
                String artist = currentMediaFile.getShow().GetPeopleInShowInRoles(new String[] { "Artist", "Artiste" });
                String albumName = album.GetAlbumName();
                String category = (genre.isEmpty() ? album.GetAlbumGenre() : genre);
                if (!albumName.isEmpty()) title = albumName + " - " + title;
                sendMessage(new Message(MessageType.CURRENT_ARTIST, (artist.isEmpty() ? album.GetAlbumArtist() : artist)));
                sendMessage(new Message(MessageType.CURRENT_CATEGORY, category));
                sendMessage(new Message(MessageType.CURRENT_TYPE, "Audio"));
            } else {
                String actors = currentMediaFile.getShow().GetPeopleInShowInRoles(new String[] { "Actor", "Acteur", "Guest", "Invité", "Special guest", "Invité spécial" });
                String channel = currentMediaFile.getChannel();
                if (!channel.isEmpty()) {
                    sendMessage(new Message(MessageType.CURRENT_CHANNEL, channel));
                    sendMessage(new Message(MessageType.CURRENT_STATION_NAME, channel + (currentMediaFile.getAiring().GetChannel() != null ? " " + currentMediaFile.getAiring().GetChannel().GetChannelDescription() : "")));
                }
                sendMessage(new Message(MessageType.CURRENT_CATEGORY, genre));
                sendMessage(new Message(MessageType.CURRENT_ACTORS, actors));
                if (currentMediaFile.isDvd()) sendMessage(new Message(MessageType.CURRENT_TYPE, "DVD")); else if (currentMediaFile.getIsLive()) sendMessage(new Message(MessageType.CURRENT_TYPE, "Live TV")); else if (currentMediaFile.getIsTvFile()) sendMessage(new Message(MessageType.CURRENT_TYPE, "TV")); else sendMessage(new Message(MessageType.CURRENT_TYPE, "Video"));
            }
            sendMessage(new Message(MessageType.CURRENT_TITLE, title));
            sendMessage(new Message(MessageType.CURRENT_DURATION, duration));
            sendMessage(new Message(MessageType.CURRENT_DESC, currentMediaFile.getDescription()));
            sendMessage(new Message(MessageType.CURRENT_YEAR, (year.isEmpty() ? "0" : year)));
            if (!episode.equalsIgnoreCase(title) || currentMediaFile.isMusicFile()) sendMessage(new Message(MessageType.CURRENT_EPISODE, episode));
            sendMessage(new Message(MessageType.CURRENT_ID, String.valueOf(currentMediaFile.getMediaFileId())));
        } catch (Exception ex) {
            logger.Debug(ex.getMessage());
        }
        currentState = newState;
        sendMessage(new Message(MessageType.PLAY_MODE, currentState.toString()));
    }
