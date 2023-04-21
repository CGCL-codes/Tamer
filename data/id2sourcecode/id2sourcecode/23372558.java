    public void exportToFile(File destination) throws FileNotFoundException, IOException {
        if (destination.exists()) {
            FileUtils.updatePermissions(destination);
        }
        File tempFolder = FileUtils.generateTempFolder(true);
        Iterator<ModListNode> it = getModList();
        while (it.hasNext()) {
            ModListNode next = it.next();
            if (next.isCompressed()) {
                Mod m = ManagerOptions.getInstance().getMod(next.getName(), next.getVersion());
                FileUtils.copyFile(new File(m.getPath()), new File(tempFolder, new File(m.getPath()).getName()));
            }
        }
        XML.modListToXml(new File(tempFolder, ModList.MODLIST_FILENAME), this);
        ZIP.createZIP(tempFolder.getAbsolutePath(), destination.getAbsolutePath());
    }
