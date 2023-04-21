    protected void zipDir(Resource dir, ZipOutputStream zOut, String vPath, int mode, ZipExtraField[] extra) throws IOException {
        if (doFilesonly) {
            logWhenWriting("skipping directory " + vPath + " for file-only archive", Project.MSG_VERBOSE);
            return;
        }
        if (addedDirs.get(vPath) != null) {
            return;
        }
        logWhenWriting("adding directory " + vPath, Project.MSG_VERBOSE);
        addedDirs.put(vPath, vPath);
        if (!skipWriting) {
            ZipEntry ze = new ZipEntry(vPath);
            int millisToAdd = roundUp ? ROUNDUP_MILLIS : 0;
            if (dir != null && dir.isExists()) {
                ze.setTime(dir.getLastModified() + millisToAdd);
            } else {
                ze.setTime(System.currentTimeMillis() + millisToAdd);
            }
            ze.setSize(0);
            ze.setMethod(ZipEntry.STORED);
            ze.setCrc(EMPTY_CRC);
            ze.setUnixMode(mode);
            if (extra != null) {
                ze.setExtraFields(extra);
            }
            zOut.putNextEntry(ze);
        }
    }
