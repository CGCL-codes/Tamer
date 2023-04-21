    private File getFile(Deviation da, String url, String filename, AtomicBoolean download, YesNoAllDialog matureMoveDialog, YesNoAllDialog overwriteDialog, YesNoAllDialog overwriteNewerDialog, YesNoAllDialog deleteEmptyDialog) {
        progress.setText("Downloading file '" + filename + "' from " + da.getArtist());
        String title = filename + " by " + da.getArtist();
        long timestamp = da.getTimestamp().getTime();
        File artPG = LocationHelper.getFile(destination, userId, da, filename);
        File artMature = LocationHelper.getFile(destinationMature, userId, da, filename);
        File art = null;
        if (da.isMature()) {
            if (artPG.exists()) {
                int resMove = matureMoveDialog.displayDialog(owner, title, "This deviation labeled as mature already exists in the main download path.\n Do you want to move the current file to the mature path?");
                if (resMove == YesNoAllDialog.CANCEL) {
                    return null;
                }
                if (resMove == YesNoAllDialog.YES) {
                    File parent = artMature.getParentFile();
                    if (!parent.mkdirs()) {
                        showMessageDialog(owner, "Unable to create '" + parent.getPath() + "'.", "Error", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                    if (artMature.exists()) {
                        int resOv = overwriteDialog.displayDialog(owner, "File already exists", "The file '" + artMature.getPath() + "' already exists. Overwrite?");
                        if (resOv == YesNoAllDialog.YES) {
                            if (!artMature.delete()) {
                                showMessageDialog(owner, "Unable to delete '" + artMature.getPath() + "'.", "Error", JOptionPane.ERROR_MESSAGE);
                                return null;
                            }
                        } else {
                            return null;
                        }
                    }
                    if (!artPG.renameTo(artMature)) {
                        showMessageDialog(owner, "Unable to move '" + artPG.getPath() + "' to '" + artMature.getPath() + "'.", "Error", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                    int resEmpty = deleteEmptyDialog.displayDialog(owner, "Delete", "Delete empty folders?");
                    if (resEmpty == YesNoAllDialog.YES) {
                        deleteEmptyFolders(artPG);
                    }
                    if (resEmpty == YesNoAllDialog.CANCEL) {
                        return null;
                    }
                    download.set(false);
                }
                if (resMove == YesNoAllDialog.NO) {
                    download.set(false);
                }
            }
            art = artMature;
        } else {
            art = artPG;
        }
        if (art.exists()) {
            if (timestamp > art.lastModified()) {
                int resOver = overwriteNewerDialog.displayDialog(owner, title, "This deviation already exist but a newer version is available. Replace?");
                if (resOver == YesNoAllDialog.CANCEL) {
                    return null;
                }
                if (resOver == YesNoAllDialog.NO) {
                    download.set(false);
                } else {
                    download.set(false);
                }
            } else {
                download.set(false);
            }
        }
        return art;
    }
