    protected void attemptToWriteToFile() {
        File selectedFile = null;
        try {
            File lastSaved;
            if (biblio) {
                lastSaved = currentEssay.getLastSavedBibFile();
            } else {
                lastSaved = currentEssay.getLastSavedAppFile();
            }
            File lastDir;
            if (lastSaved != null) {
                lastDir = lastSaved;
            } else {
                lastDir = OHCEssay.getLastDirectoryForAnyEssay();
            }
            selectedFile = FileUtils.selectFileToSave("Rich Text Format", "rtf", lastDir, lastSaved);
            if (null != selectedFile) {
                writeFile(selectedFile);
            }
        } catch (FileNotFoundException fnf) {
            JOptionPane.showMessageDialog(null, "Trying to write to the following file failed.\n" + selectedFile + "\n" + "The file might be read-only or it might be open in another program.\n" + "The essay was not exported.\n" + "You should export the essay to a different file.\n\n" + "Exception text follows:" + fnf.toString(), "Couldn't write to file!", JOptionPane.ERROR_MESSAGE);
        } catch (BadLocationException ble) {
            ble.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
