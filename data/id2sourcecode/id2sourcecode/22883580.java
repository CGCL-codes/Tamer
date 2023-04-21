    public void loadSong() {
        try {
            if (TGConfig.SONG_URL != null) {
                URL url = new URL(TGConfig.SONG_URL);
                InputStream stream = getInputStream(url.openStream());
                TGSong song = TGFileFormatManager.instance().getLoader().load(TuxGuitar.instance().getSongManager().getFactory(), stream);
                TuxGuitar.instance().fireNewSong(song);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            TuxGuitar.instance().newSong();
        }
    }
