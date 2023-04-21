    private void indexDirectory(File dir, PhotoFolder folder) {
        if (createDirIndexers) {
            dirIndexers.add(new DirectoryIndexer(dir, folder, vol));
        }
        Map<String, PhotoFolder> folders = new HashMap<String, PhotoFolder>();
        for (PhotoFolder f : folder.getSubfolders()) {
            folders.put(f.getName(), f);
        }
        File[] dirEntries = dir.listFiles();
        for (File d : dirEntries) {
            if (d.isDirectory() && !d.getName().equals(".photovault_volume")) {
                folderCount++;
                PhotoFolder f = null;
                if (folders.containsKey(d.getName())) {
                    f = folders.remove(d.getName());
                } else {
                    try {
                        CreatePhotoFolderCommand cmd = new CreatePhotoFolderCommand(folder, d.getName(), "");
                        StringBuffer pathBuf = new StringBuffer(folder.getExternalDir().getPath());
                        if (pathBuf.length() > 0) {
                            pathBuf.append("/");
                        }
                        pathBuf.append(d.getName());
                        cmd.setExtDir(folder.getExternalDir().getVolume(), pathBuf.toString());
                        cmdHandler.executeCommand(cmd);
                        f = (PhotoFolder) session.merge(cmd.getCreatedFolder());
                        newFolderCount++;
                    } catch (CommandException ex) {
                        log.error("Error while creating folder", ex);
                    }
                }
                indexDirectory(d, f);
            }
        }
        for (PhotoFolder f : folders.values()) {
            try {
                DeletePhotoFolderCommand deleteCmd = new DeletePhotoFolderCommand(f.getUuid());
                cmdHandler.executeCommand(deleteCmd);
                deletedFolderCount++;
            } catch (CommandException ex) {
                log.error("Error while deleting folder: ", ex);
            }
        }
    }
