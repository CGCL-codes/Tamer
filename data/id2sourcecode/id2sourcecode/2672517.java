    private static void checkTempGames(long cutoff) throws InternalErrorException {
        try {
            File dir = new File(getTemporaryDirectory());
            File[] games = dir.listFiles();
            for (int i = 0; i < games.length; i++) {
                long modtime = games[i].lastModified();
                if (modtime < cutoff) {
                    if (games[i].isDirectory()) {
                        File[] delfiles = games[i].listFiles();
                        int errcount = 0;
                        for (int j = 0; j < delfiles.length; j++) {
                            if (!delfiles[j].delete()) errcount++;
                        }
                        if (!games[i].delete()) errcount++;
                        if (errcount > 0) {
                            SystemLog.warning("Unable to delete temporary game " + games[i].getAbsolutePath());
                        }
                    } else {
                        if (games[i].getName().equals(LAST_GAME_FILE_NAME)) games[i].delete();
                    }
                }
            }
        } catch (Exception e) {
            InternalErrorException iee = new InternalErrorException(e);
            throw iee;
        }
    }
