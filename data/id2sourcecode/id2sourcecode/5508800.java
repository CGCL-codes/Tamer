    @Override
    public void widgetSelected(SelectionEvent e) {
        final MatchesPlaylist playlist = GroofyApp.getInstance().getMatchWindow().getContents();
        if (playlist == null) return;
        if (playlist.getPlaylistName() == null || playlist.getPlaylistName().equals("")) {
            GroofyApp.getInstance().showError(Messages.getString("MainWindow.PlaylistEmpty"));
            return;
        }
        new Job("Saving new playlist in GrooveShark") {

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

            /**
			 * Searches a playlist with the name playlistName among 
			 * the lists of the user with ID userID  
			 * @param userID
			 * @param playlist
			 * @return the playlist if found, null otherwise
			 */
            private JsonPlaylists.Playlist searchUserPlaylist(String userID, String playlistName) {
                HashMap<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("userID", userID);
                try {
                    String response = JGroove.callMethodHttps(parameters, "userGetPlaylists");
                    GroofyLogger.getInstance().log(String.format("Get playlist from user response : %s", response), false);
                    JsonPlaylists result = new Gson().fromJson(response, JsonPlaylists.class);
                    for (JsonPlaylists.Playlist p : result.result.Playlists) {
                        if (p.Name.toLowerCase().equals(playlistName.toLowerCase())) {
                            return p;
                        }
                    }
                } catch (IOException e1) {
                    GroofyLogger.getInstance().logException(e1);
                }
                return null;
            }

            /**
			 * Logs-in in the Grooveshark service with the given username and password
			 * 
			 * @return the userID of the successfully logged user, 
			 * 			or null if the login did not succeed 
			 */
            private String login(String username, String password) {
                HashMap<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("username", username);
                parameters.put("password", password);
                String userID = null;
                try {
                    String response = JGroove.callMethodHttps(parameters, "authenticateUser");
                    GroofyLogger.getInstance().log(String.format("Login server response: %s", response));
                    JsonAuthenticate result = new Gson().fromJson(response, JsonAuthenticate.class);
                    userID = result.result.get("userID");
                } catch (IOException e1) {
                    GroofyLogger.getInstance().logException(e1);
                }
                return (userID != null && !userID.equals("0")) ? userID : null;
            }

            /**
			 * Gets the list of song IDs of the playlist
			 * @param playlist
			 * @return the list of songIDs
			 */
            private ArrayList<String> getSongsList(final MatchesPlaylist playlist) {
                ArrayList<String> songIDs = new ArrayList<String>();
                for (TrackMatch m : playlist.getMatches()) {
                    if (m.getMatch() != null) {
                        songIDs.add(m.getMatch().getSongID());
                    }
                }
                return songIDs;
            }

            /**
			 * Creates a new playlist with the name playlistName using 
			 * the list of songs specified by songIDs
			 * 
			 * @param playlistName
			 * @param songIDs
			 * @return true if the creation succeed, false otherwise
			 */
            private boolean createPlaylist(final String playlistName, ArrayList<String> songIDs) {
                HashMap<String, Object> parameters;
                parameters = new HashMap<String, Object>();
                parameters.put("playlistName", playlistName);
                parameters.put("songIDs", songIDs);
                try {
                    String response = JGroove.callMethod(parameters, "createPlaylist");
                    GroofyLogger.getInstance().log(String.format("Create playlist response: %s", response));
                    if (response.contains("\"fault\":{\"code\"")) {
                        return false;
                    }
                } catch (IOException e2) {
                    GroofyLogger.getInstance().logException(e2);
                    return false;
                }
                return true;
            }

            /**
			 * Replaces the contents of the playlist specified by playlistID using
			 * the list of songs specified by songIDs
			 * 
			 * @deprecated It seems that this service no longer works (returns server error 500)
			 *  
			 * @param playlistID
			 * @param songIDs
			 * @return true if the replace succeed, false otherwise
			 */
            private boolean replacePlaylist(final Integer playlistID, ArrayList<String> songIDs) {
                HashMap<String, Object> parameters;
                parameters = new HashMap<String, Object>();
                parameters.put("playlistID", playlistID);
                parameters.put("songIDs", songIDs);
                try {
                    String response = JGroove.callMethod(parameters, "overwriteExistingPlaylist");
                    GroofyLogger.getInstance().log(String.format("Overwrite playlist response: %s", response), false);
                    if (response.contains("\"fault\":{\"code\"")) {
                        return false;
                    }
                } catch (IOException e2) {
                    GroofyLogger.getInstance().logException(e2);
                    return false;
                }
                return true;
            }

            /**
			 * Deletes the playlist specified by playlistID 
			 * 
			 * @param playlistID
			 * @return true if the replace succeed, false otherwise
			 */
            private boolean deletePlaylist(final JsonPlaylists.Playlist playlist) {
                HashMap<String, Object> parameters;
                parameters = new HashMap<String, Object>();
                parameters.put("playlistID", playlist.PlaylistID);
                parameters.put("name", playlist.Name);
                try {
                    String response = JGroove.callMethod(parameters, "deletePlaylist");
                    GroofyLogger.getInstance().log(String.format("Delete playlist response: %s", response));
                    if (response.contains("\"fault\":{\"code\"")) {
                        GroofyApp.getInstance().showError("Unable to delete playlist");
                    }
                } catch (IOException e2) {
                    GroofyLogger.getInstance().logException(e2);
                    return false;
                }
                return true;
            }
        }.schedule();
    }
