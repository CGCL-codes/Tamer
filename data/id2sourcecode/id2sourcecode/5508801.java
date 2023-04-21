            @Override
            public JobStatus run() {
                setDescription("Checking playlist...");
                ArrayList<String> songIDs = getSongsList(playlist);
                if (songIDs.size() != playlist.getMatches().size()) {
                    int result = GroofyApp.getInstance().showYesNoDialog("Proceed?", Messages.getString("MainWindow.TracksIncomplete"));
                    if (result == SWT.NO) {
                        return JobStatus.CANCELLED;
                    }
                }
                String username = getUsernameFromPreferences();
                String password = getPasswordFromPreferences();
                String userID = null;
                setDescription("Logging in...");
                boolean logged = false;
                int res = SWT.NONE;
                do {
                    userID = login(username, password);
                    if (userID != null) {
                        logged = true;
                    } else {
                        LoginWindowRunnable runnable = new LoginWindowRunnable(res == SWT.NONE ? false : true);
                        Display.getDefault().syncExec(runnable);
                        res = runnable.getResult();
                        if (res == SWT.OK) {
                            username = runnable.getUsername();
                            password = runnable.getPassword();
                        } else {
                            return JobStatus.CANCELLED;
                        }
                    }
                } while (!logged);
                setDescription("Checking user playlists...");
                JsonPlaylists.Playlist jsonPlaylist = searchUserPlaylist(userID, playlist.getPlaylistName());
                setDescription("Creating playlist...");
                if (jsonPlaylist != null) {
                    int replaceDialogRes = GroofyApp.getInstance().showYesNoDialog("Overwrite playlist?", String.format("Playlist \"%s\" already exists. Do you want to remove it first?", playlist.getPlaylistName()));
                    if (replaceDialogRes == SWT.YES && !deletePlaylist(jsonPlaylist)) {
                        GroofyApp.getInstance().showError("Unable to remove previous playlist.\nPlaylist will be duplicated.");
                    }
                }
                if (createPlaylist(playlist.getPlaylistName(), songIDs)) {
                    GroofyApp.getInstance().showInfo("Succes!", Messages.getString("MainWindow.PlaylistCreated"));
                } else {
                    GroofyApp.getInstance().showError("Unable to create playlist. An unexpected error occurred.\nYou can find more info in the console log.");
                    return JobStatus.ERROR;
                }
                return JobStatus.OK;
            }
