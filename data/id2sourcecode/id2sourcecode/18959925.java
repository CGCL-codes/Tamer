    public Vector load() {
        ObjectInputStream ourBuffer;
        FileInputStream ourStream = null;
        try {
            ourStream = new FileInputStream(SAVE_FILE_NAME);
        } catch (FileNotFoundException exc) {
            return null;
        }
        StatusBar state = Buttress.getButtress().getMyGui().status;
        state.setMessage("Loading Saved Feeds and Filters...");
        state.setValue(1, 3);
        try {
            ourBuffer = new ObjectInputStream(ourStream);
        } catch (Exception exc) {
            ConsoleDialog.writeError("Couldn't create the save file input " + "stream.", exc);
            return null;
        }
        state.setMessage("Reading Save File...");
        state.setValue(2, 3);
        try {
            Object loaded;
            while (true) {
                loaded = ourBuffer.readObject();
                if (loaded instanceof String) {
                    backupVersions.add(loaded.toString());
                } else if (loaded instanceof Rss) {
                    versionsAvailable.add(loaded);
                }
            }
        } catch (FileNotFoundException exc) {
            ConsoleDialog.writeError("The " + SAVE_FILE_NAME + " file was not found.", exc);
            return null;
        } catch (EOFException exc) {
        } catch (ClassNotFoundException exc) {
            ConsoleDialog.writeError("The " + SAVE_FILE_NAME + "file was written incorrectly.", exc);
            return null;
        } catch (InvalidClassException exc) {
            updateClassModified(exc);
        } catch (IOException exc) {
            ConsoleDialog.writeError("There was an error  while reading the " + "save file (" + exc.getLocalizedMessage() + ").", exc);
        }
        state.setMessage("Loading Saved Feeds and Filters...");
        state.setValue(3, 3);
        if (updateClassChanged) {
            return extractRssData(backupVersions);
        } else {
            return versionsAvailable;
        }
    }
