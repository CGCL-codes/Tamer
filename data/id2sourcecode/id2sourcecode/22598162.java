    public File getOutputFileFromUser(File inputFile) {
        final String msgFileExists = "The file already exists. Do you want to overwrite?";
        JFileChooser jfc = new JFileChooser();
        jfc.setMultiSelectionEnabled(false);
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        SimplerFileFilter defaultFileFilter = new SimplerFileFilter(".iso", "CD/DVD image (*.iso)");
        jfc.addChoosableFileFilter(defaultFileFilter);
        jfc.addChoosableFileFilter(new SimplerFileFilter(".img", "Raw image (*.img)"));
        jfc.addChoosableFileFilter(new SimplerFileFilter(".bin", "Binary file (*.bin)"));
        jfc.addChoosableFileFilter(new SimplerFileFilter(".dmg", "Mac OS X read/write disk image (*.dmg)"));
        jfc.setFileFilter(defaultFileFilter);
        jfc.setDialogTitle("Select your output file");
        if (inputFile != null) {
            String name = inputFile.getName();
            String defaultOutName = name;
            int lastDotIndex = defaultOutName.lastIndexOf(".");
            if (lastDotIndex >= 0) {
                defaultOutName = defaultOutName.substring(0, lastDotIndex);
            }
            jfc.setSelectedFile(new File(inputFile.getParentFile(), defaultOutName));
        }
        while (true) {
            if (jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                final File saveFile;
                FileFilter selectedFileFilter = jfc.getFileFilter();
                if (selectedFileFilter instanceof SimplerFileFilter) {
                    SimplerFileFilter sff = (SimplerFileFilter) selectedFileFilter;
                    if (!selectedFile.getName().endsWith(sff.getExtension())) saveFile = new File(selectedFile.getParentFile(), selectedFile.getName() + sff.getExtension()); else saveFile = selectedFile;
                } else {
                    saveFile = selectedFile;
                }
                if (!saveFile.exists()) return saveFile; else if (JOptionPane.showConfirmDialog(null, msgFileExists, "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    return saveFile;
                }
            } else return null;
        }
    }
