    private String[] loadFileMaps() {
        String extension = GameDatabase.getMapExtension(roomData.getChannel(), roomData.getModName());
        String path = GameDatabase.getFullMapPath(roomData.getChannel(), roomData.getModName());
        Logger.log(LogTypes.LOG, "Loading maps from: " + path);
        if (path.endsWith("\\") || path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        File mapdir = new File(path);
        if (!mapdir.isDirectory()) {
            return new String[0];
        }
        File[] files = mapdir.listFiles();
        Vector<String> names = new Vector<String>();
        for (File f : files) {
            if (f.isFile()) {
                String tmp = f.getName();
                if (tmp.toLowerCase().endsWith(extension.toLowerCase())) {
                    names.add(tmp.substring(0, tmp.length() - (extension.length() + 1)));
                }
            }
        }
        return names.toArray(new String[names.size()]);
    }
