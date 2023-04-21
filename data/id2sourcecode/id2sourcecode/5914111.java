    public static boolean checkGameFile(String game, boolean updateFile) {
        File gameFile = new File(Utilities.GAME_SETTINGS_FILE_DIR, game + GameSelectionDialog.SETTINGS_FILE_TYPE);
        File localVersionFile = new File(Utilities.GAME_SETTINGS_FILE_DIR, "version.properties");
        Properties gameFileVersions = getLocalGameInfo();
        String remoteVersion = gameFilesAvailable.getProperty(game + ".version");
        assert remoteVersion != null : "No remote version for game '" + game + "'";
        Properties gameFilesAvailable = getRemoteGameInfo();
        if (gameFile.exists()) {
            try {
                String localVersion = gameFileVersions.getProperty(game + ".version");
                if (localVersion != null && localVersion.equals(remoteVersion)) return true;
            } catch (IllegalStateException e) {
                if (gameFile.exists()) return true;
                throw e;
            }
        }
        if (gameFile.exists() && !updateFile) return false;
        Writer writer = null;
        try {
            FileUtil.writeBytes(gameFile, readGameFile(gameFile.getName()));
            gameFileVersions.setProperty(game + ".version", remoteVersion);
            gameFileVersions.setProperty(game + ".name", gameFilesAvailable.getProperty(game + ".name"));
            String games = gameFileVersions.getProperty("games");
            if (games == null || !games.contains(game)) gameFileVersions.setProperty("games", games == null ? game : games + "," + game);
            writer = new BufferedWriter(new FileWriter(localVersionFile));
            gameFileVersions.store(writer, "Version properties for each of the locally available game files");
            return true;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error reading game file from server.", e);
            if (gameFile.exists()) return false;
            throw new IllegalStateException("Unable to access the game files from RPTools.");
        } finally {
            if (writer != null) try {
                writer.close();
            } catch (IOException e) {
            }
        }
    }
