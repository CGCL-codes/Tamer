    private void synchUI(int selIndex) {
        if (selIndex < 0 || selIndex >= songs.size()) {
            infoSong.setText("");
            infoAlbum.setText("");
            infoArtist.setText("");
            infoLength.setText("");
            cover.setImage(jukebox);
        } else {
            Song song = songs.get(selIndex);
            infoSong.setText(song.getTitle());
            infoAlbum.setText(song.getAlbum().getName());
            infoArtist.setText(song.getAuthor().getName());
            infoLength.setText(song.getFormattedDuration() + " - " + song.getChannels() + "ch - " + song.getBitrate() + "kbps");
            Image img = imgGfx.loadImage(song.getAlbum().getCover());
            if (img == null) {
                cover.setImage(jukebox);
            } else {
                cover.setImage(img);
            }
        }
        for (int i = 0; i < labels.length; i++) {
            int songIndex = page * pageSize + i;
            if (songIndex == playlist.getSelectedIndex()) {
                labelIcon[i].setImage(currentSong);
            } else if (songIndex == selIndex) {
                labelIcon[i].setImage(selSong);
            } else {
                labelIcon[i].setImage(otherSong);
            }
        }
    }
