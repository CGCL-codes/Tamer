    private void saveImage(File directory) {
        if (displayedImageData != null) {
            JFileChooser chooser = new JFileChooser();
            String channelName = (String) channels.iterator().next();
            String fileName = channelName.replace("/", " - ");
            if (!fileName.endsWith(".jpg")) {
                fileName += ".jpg";
            }
            chooser.setSelectedFile(new File(directory, fileName));
            chooser.setFileFilter(new FileFilter() {

                public boolean accept(File f) {
                    return (f.isDirectory() || f.getName().toLowerCase().endsWith(".jpg"));
                }

                public String getDescription() {
                    return "JPEG Image Files";
                }
            });
            int returnVal = chooser.showSaveDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File outFile = chooser.getSelectedFile();
                if (outFile.exists()) {
                    int overwriteReturn = JOptionPane.showConfirmDialog(null, outFile.getName() + " already exists. Do you want to replace it?", "Replace image?", JOptionPane.YES_NO_OPTION);
                    if (overwriteReturn == JOptionPane.NO_OPTION) {
                        saveImage(outFile.getParentFile());
                        return;
                    }
                }
                try {
                    FileOutputStream out = new FileOutputStream(outFile);
                    out.write(displayedImageData);
                    out.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Filed to write image file.", "Save Image Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        }
    }
