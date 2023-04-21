    private void deleteTemporaryState(GameId id) throws PermissionsException, NoSuchDataException {
        String fname = getTemporaryDirectoryName(id);
        File f = new File(fname);
        if ((!f.exists()) || (!f.isDirectory())) {
            SystemLog.error("Invalid game id " + id);
            throw new NoSuchDataException("Invalid game id");
        }
        boolean flag = true;
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (!files[i].delete()) flag = false;
        }
        if (flag) flag = f.delete();
        if (!flag) {
            SystemLog.error("Could not delete one or more files for game " + id);
            throw new PermissionsException("Could not delete game");
        }
    }
