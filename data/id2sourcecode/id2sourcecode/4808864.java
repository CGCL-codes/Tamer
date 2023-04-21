    public void saveSnapshot(final Component component) throws java.awt.AWTException, IOException {
        BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        component.paintAll(image.createGraphics());
        File defaultFolder = _fileChooser.getCurrentDirectory();
        File defaultFile = new File(defaultFolder, "Untitled.png");
        _fileChooser.setSelectedFile(defaultFile);
        int status = _fileChooser.showSaveDialog(component);
        switch(status) {
            case JFileChooser.CANCEL_OPTION:
                break;
            case JFileChooser.APPROVE_OPTION:
                File fileSelection = _fileChooser.getSelectedFile();
                if (fileSelection.exists()) {
                    int confirm = displayConfirmDialog(component, "Overwrite Confirmation", "The selected file:  " + fileSelection + " already exists! \n Overwrite selection?");
                    if (confirm == NO_OPTION) {
                        saveSnapshot(component);
                        return;
                    }
                }
                ImageIO.write(image, "png", fileSelection);
                break;
            case JFileChooser.ERROR_OPTION:
                break;
        }
    }
