    public boolean save(final View view, String path, final boolean rename, boolean disableFileStatusCheck) {
        if (isPerformingIO()) {
            return false;
        }
        setBooleanProperty(BufferIORequest.ERROR_OCCURRED, false);
        if (path == null && getFlag(NEW_FILE)) return saveAs(view, rename);
        if (path == null && file != null) {
            long newModTime = file.lastModified();
            if (newModTime != modTime && jEdit.getBooleanProperty("view.checkModStatus")) {
                Object[] args = { this.path };
            }
        }
        EditBus.send(new BufferUpdate(this, view, BufferUpdate.SAVING));
        setPerformingIO(true);
        final String oldPath = this.path;
        final String oldSymlinkPath = symlinkPath;
        final String newPath = path == null ? this.path : path;
        VFS vfs = VFSManager.getVFSForPath(newPath);
        if (!checkFileForSave(view, vfs, newPath)) {
            setPerformingIO(false);
            return false;
        }
        Object session = vfs.createVFSSession(newPath, view);
        if (session == null) {
            setPerformingIO(false);
            return false;
        }
        unsetProperty("overwriteReadonly");
        unsetProperty("forbidTwoStageSave");
        try {
            VFSFile file = vfs._getFile(session, newPath, view);
            if (file != null) {
                boolean vfsRenameCap = (vfs.getCapabilities() & VFS.RENAME_CAP) != 0;
                if (!file.isWriteable()) {
                    Log.log(Log.WARNING, this, "Buffer saving : File " + file + " is readOnly");
                    if (vfsRenameCap) {
                        Log.log(Log.DEBUG, this, "Buffer saving : VFS can rename files");
                        String savePath = vfs._canonPath(session, newPath, view);
                        if (!MiscUtilities.isURL(savePath)) savePath = MiscUtilities.resolveSymlinks(savePath);
                        savePath = vfs.getTwoStageSaveName(savePath);
                        if (savePath == null) {
                            Log.log(Log.DEBUG, this, "Buffer saving : two stage save impossible because path is null");
                            VFSManager.error(view, newPath, "ioerror.save-readonly-twostagefail", null);
                            setPerformingIO(false);
                            return false;
                        }
                    } else {
                        Log.log(Log.WARNING, this, "Buffer saving : file is readonly and vfs cannot do two stage save");
                        VFSManager.error(view, newPath, "ioerror.write-error-readonly", null);
                        setPerformingIO(false);
                        return false;
                    }
                } else {
                    String savePath = vfs._canonPath(session, newPath, view);
                    if (!MiscUtilities.isURL(savePath)) savePath = MiscUtilities.resolveSymlinks(savePath);
                    savePath = vfs.getTwoStageSaveName(savePath);
                }
            }
        } catch (IOException io) {
            VFSManager.error(view, newPath, "ioerror", new String[] { io.toString() });
            setPerformingIO(false);
            return false;
        } finally {
            try {
                vfs._endVFSSession(session, view);
            } catch (IOException io) {
                VFSManager.error(view, newPath, "ioerror", new String[] { io.toString() });
                setPerformingIO(false);
                return false;
            }
        }
        if (!vfs.save(view, this, newPath)) {
            setPerformingIO(false);
            return false;
        }
        int check = jEdit.getIntegerProperty("checkFileStatus");
        return true;
    }
