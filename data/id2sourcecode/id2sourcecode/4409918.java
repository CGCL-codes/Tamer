    public void receivePacket(JSPacket jsp) {
        JSPacket jsPacket = jsp;
        FileInitPacket packet;
        if (jsp.modulePacket instanceof FileInitPacket) {
            packet = (FileInitPacket) jsp.modulePacket;
            showFrame();
            File tmpFile = new File(packet.filename);
            String tmpFilename = tmpFile.getName();
            int selectedValue = JOptionPane.showConfirmDialog(guiWindow.getContentPane(), "Would you like to accept the file " + tmpFilename + " from " + packet.sender, "Incoming File", JOptionPane.YES_NO_OPTION);
            if (selectedValue != JOptionPane.YES_OPTION) return;
            JFileChooser fileSaver = new JFileChooser();
            fileSaver.setDialogTitle("Save " + tmpFilename + "to where?");
            fileSaver.setAcceptAllFileFilterUsed(false);
            fileSaver.setCurrentDirectory(new File("."));
            fileSaver.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            String selectedFile;
            int result = fileSaver.showSaveDialog(guiWindow);
            while (1 == 1) {
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFile = new String(fileSaver.getSelectedFile().getPath());
                    File file = new File(selectedFile);
                    if (file.isDirectory() == false || file.exists() == false) {
                        JOptionPane.showMessageDialog(fileSaver, "Oops!, that location doesnt exists!");
                        result = fileSaver.showSaveDialog(guiWindow);
                    } else {
                        file = new File(selectedFile + tmpFilename);
                        if (file.exists() == true) {
                            selectedValue = JOptionPane.showConfirmDialog(guiWindow.getContentPane(), "File exists there already! Overwrite the file? No, select a different location, or cancel the file", "Overwrite File?", JOptionPane.YES_NO_CANCEL_OPTION);
                            if (selectedValue != JOptionPane.YES_OPTION) {
                                file.delete();
                                break;
                            }
                            if (selectedValue != JOptionPane.NO_OPTION) {
                                result = fileSaver.showSaveDialog(guiWindow);
                            } else if (selectedValue == JOptionPane.CANCEL_OPTION || selectedValue == JOptionPane.CLOSED_OPTION) {
                                return;
                            }
                        } else {
                            break;
                        }
                    }
                } else return;
            }
            addReceive(packet.address, new String(selectedFile + "\\" + tmpFilename), jsp.destination.getUsername(), packet.sender);
        }
    }
