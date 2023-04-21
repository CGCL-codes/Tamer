    private SongImpl analyzeSong(ScanCollectionState scs, File file) {
        SongImpl song;
        try {
            AudioFile mp3 = AudioFileIO.read(file);
            AuthorImpl author = createOrGetAuthor(scs, process(mp3.getTag().getFirstArtist()));
            AlbumImpl album = createOrGetAlbum(scs, author, file.getParentFile(), process(mp3.getTag().getFirstAlbum()));
            int track = 0;
            try {
                track = Integer.valueOf(mp3.getTag().getFirstTrack());
            } catch (NumberFormatException e) {
            }
            song = new SongImpl(scs.nextSongId++, album, author, file, process(mp3.getTag().getFirstTitle()), track, (long) (mp3.getPreciseLength() * 1000), mp3.getChannelNumber(), mp3.getBitrate(), mp3.isVbr(), file.length(), file.lastModified());
            album.addSong(song);
        } catch (Exception e) {
            AuthorImpl author = createOrGetAuthor(scs, "");
            AlbumImpl album = createOrGetAlbum(scs, author, null, "");
            song = new SongImpl(scs.nextSongId++, album, author, file, "", 0, 0, 0, 0, false, file.length(), file.lastModified());
        }
        return song;
    }
