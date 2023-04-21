    private String[] loadPK3Maps() {
        String pk3FindPath = GameDatabase.getInstallPath(roomData.getChannel()) + GameDatabase.getPK3FindPath(roomData.getChannel(), roomData.getModName());
        Vector<String> names = new Vector<String>();
        ArrayList<File> pk3Files = new ArrayList<File>();
        getPK3Files(pk3Files, new File(pk3FindPath));
        for (File pk3File : pk3Files) {
            try {
                ZipFile zf = new ZipFile(pk3File);
                for (Enumeration entries = zf.entries(); entries.hasMoreElements(); ) {
                    String zipEntryName = ((ZipEntry) entries.nextElement()).getName();
                    String mapFileName = getMapNameFromEntry(zipEntryName);
                    if (mapFileName != null) {
                        names.add(mapFileName);
                    }
                }
            } catch (IOException e) {
            }
        }
        return names.toArray(new String[names.size()]);
    }
