    @Override
    public boolean setup(MonitorableImpl monitor, Map<String, Object> properties) {
        monitor.setStarted(true);
        JFileChooser chooser = new JFileChooser(recent.getMostRecentFile());
        chooser.addChoosableFileFilter(SPSUtils.XML_FILE_FILTER);
        File file = null;
        while (true) {
            int response = chooser.showSaveDialog(session.getArchitectFrame());
            if (response != JFileChooser.APPROVE_OPTION) {
                return false;
            }
            file = chooser.getSelectedFile();
            String fileName = file.getName();
            if (!fileName.endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            if (file.exists()) {
                response = JOptionPane.showConfirmDialog(null, "The file " + file.getPath() + " already exists. Do you want to overwrite it?", "File Exists", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    break;
                }
            } else {
                break;
            }
        }
        logger.debug("Saving to file: " + file.getName() + "(" + file.getPath() + ")");
        recent.putRecentFileName(file.getAbsolutePath());
        properties.put(FILE_KEY, file);
        return true;
    }
