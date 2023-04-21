    public boolean renameFileOld(boolean overwrite) throws IOException, FileAlreadyExistsException {
        final String oldPath = file.getAbsolutePath();
        final String newPath = getNewAbsolutePath();
        final File destination = new File(newPath);
        if (renamer.getConfig().getBoolean(REPLACE_ORIGINAL_FILES)) {
            boolean renamed = new File(oldPath).renameTo(destination);
            File oldFile = new File(oldPath);
            for (int i = 0; i < 3 && oldFile.isFile() && !destination.isFile(); i++) {
                Log.defaultLogger().warning("ERROR! could not remane file '" + oldPath + "' " + "to '" + newPath + "'. trying again...");
                renamed = new File(oldPath).renameTo(new File(newPath));
            }
            if (oldFile.isFile() || !destination.isFile()) {
                Log.defaultLogger().warning("APPLY HACK: copy file '" + oldPath + "' to '" + newPath + "'...");
                Util.copyFile(new File(oldPath), destination);
                if (destination.isFile()) {
                    Log.defaultLogger().warning("HACK Successful, delete source file '" + oldPath + "'!");
                    new File(oldPath).delete();
                } else {
                    Log.defaultLogger().warning("HACK Unsuccessful. '" + destination + "' could not be created!!");
                }
            }
            if (oldFile.isFile() || !destination.isFile()) {
                Log.defaultLogger().warning("ERROR! could not remane file '" + oldPath + "' to '" + newPath + "'. skipping :-(.");
                return false;
            }
            return renamed;
        }
        if (!overwrite && destination.exists()) {
            throw new FileAlreadyExistsException(destination);
        }
        Util.copyFile(file, destination);
        return true;
    }
