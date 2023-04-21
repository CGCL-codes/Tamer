    private int makeCopyOfDir(FileDescriptor fd, int destDirId) {
        if (!fd.isDirectory) {
            return NOTHING;
        }
        int dirId = destDirId;
        while (dirId != ROOT_DIR) {
            if (fd.id == dirId) {
                return NOTHING;
            }
            FileDescriptor f = getDescriptorById(dirId);
            dirId = f.parentDirId;
        }
        String newName = (destDirId == fd.parentDirId) ? ResourceBundle.getString("nb-copyof") : "";
        newName += fd.name;
        int newId = makeDir(newName, destDirId);
        Vector ids = getDirectoryListing(fd.id, 0);
        for (int i = 0; i < ids.size(); i++) {
            FileDescriptor f = (FileDescriptor) ids.elementAt(i);
            if (!f.name.equals("..")) {
                makeCopy(f.id, newId, false);
            }
        }
        return newId;
    }
