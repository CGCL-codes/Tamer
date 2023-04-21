    private void DownloadButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String fileName = "statcato_" + currentVersion + ".zip";
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(fileName));
        ExtensionFileFilter ZipFilter = new ExtensionFileFilter("Zip (*.zip)", "zip");
        fc.addChoosableFileFilter(ZipFilter);
        fc.setAcceptAllFileFilterUsed(false);
        int returnValue = fc.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String path = file.getPath();
            if (file.exists()) {
                Object[] options = { "Overwrite file", "Cancel" };
                int choice = JOptionPane.showOptionDialog(this, "The specified file already exists.  Overwrite existing file?", "Overwrite file?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
                if (choice != 0) return;
            }
            DownloadFile d = new DownloadFile("http://www.statcato.org/versions/statcato_" + currentVersion + ".zip");
            int totalDownload = d.download(path);
            if (totalDownload <= 0) {
                JOptionPane.showMessageDialog(this, "Download failed.", "Download error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "The latest version is downloaded to " + path, "Download complete", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }
