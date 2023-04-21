    private boolean deleteExistingFile(String saveFilename) throws Exception {
        File file = new File(saveFilename);
        if (file.exists()) {
            int option = JOptionPane.showConfirmDialog(ServiceManager.getManager().getMainFrame(), "The file '" + saveFilename + "' exists already.  Overwrite it with the download file?", "Confirmation", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (option != JOptionPane.YES_OPTION) {
                return false;
            }
            if (file.delete() == false) {
                String msg = "Failed to delete the old file '";
                msg += saveFilename + ".'";
                JOptionPane.showMessageDialog(ServiceManager.getManager().getMainFrame(), msg, "Failed To Delete File", JOptionPane.ERROR_MESSAGE);
                throw new Exception("Failed To Delete File");
            }
            mDownload.appendLog("Old file deleted.");
        }
        return true;
    }
