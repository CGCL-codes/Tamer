    public static final void openFile(IKittenView kittenView, KittenProperties kittenProperties) {
        JFrame frame = (JFrame) kittenView.getParentFrame();
        JFileChooser fc = new JFileChooser(kittenProperties.getInstallDirectory() + System.getProperty("file.separator") + "kitten" + System.getProperty("file.separator") + "scripts");
        fc.setFont(fileChooserFont);
        int returnVal = fc.showOpenDialog(frame);
        File selFile = fc.getSelectedFile();
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (readFile(selFile) != null) {
                kittenView.writeOpenFile2Screen(readFile(selFile));
                kittenView.setOpenedFile(selFile.getPath());
            }
        }
    }
