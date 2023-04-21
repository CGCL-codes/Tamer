    private void savePlaylist() {
        File suggested;
        if (lastSavedPlaylist != null) suggested = lastSavedPlaylist; else if (lastOpenedPlaylist != null) suggested = lastOpenedPlaylist; else suggested = new File(CommonUtils.getCurrentDirectory(), "frostwire.m3u");
        File selFile = FileChooserHandler.getSaveAsFile(getComponent(), I18nMarker.marktr("Save Playlist As"), suggested, new PlaylistListFileFilter());
        if (selFile == null) return;
        if (selFile.exists() && !selFile.equals(lastOpenedPlaylist)) {
            DialogOption choice = GUIMediator.showYesNoMessage(I18n.tr("Warning: a file with the name {0} already exists in the folder. Overwrite this file?", selFile.getName()), QuestionsHandler.PLAYLIST_OVERWRITE_OK, DialogOption.NO);
            if (choice != DialogOption.YES) return;
        }
        String path = selFile.getPath();
        try {
            path = FileUtils.getCanonicalPath(selFile);
        } catch (IOException ignored) {
            LOG.warn("unable to get canonical path for file: " + selFile, ignored);
        }
        if (!path.toLowerCase().endsWith(".m3u")) path += ".m3u";
        savePlaylist(path);
    }
