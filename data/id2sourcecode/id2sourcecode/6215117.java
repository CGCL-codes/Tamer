    @SuppressWarnings("unchecked")
    @Override
    public final void saveCueFile() {
        String newLine = System.getProperty("line.separator");
        if (GUI.getArtistField().getText().equals("")) {
            JOptionPane.showMessageDialog(GUI.getContent(), "You must fill in an Artist!", "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (GUI.getTitleField().getText().equals("")) {
            JOptionPane.showMessageDialog(GUI.getContent(), "You must fill in a Title!", "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (GUI.getFileField().getText().equals("")) {
            JOptionPane.showMessageDialog(GUI.getContent(), "You must choose your file!", "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (GUI.getRowData().size() < 1) {
            JOptionPane.showMessageDialog(GUI.getContent(), "You must fill in at least 1 track!", "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String cueFile;
        String track = "";
        String completeTrack = "";
        cueFile = "FILE \"" + GUI.getFileField().getText() + "\" MP3" + newLine;
        cueFile += "PERFORMER \"" + GUI.getArtistField().getText() + "\"" + newLine;
        cueFile += "TITLE \"" + GUI.getTitleField().getText() + "\"" + newLine;
        for (int i = 0; i < GUI.getRowData().size(); i++) {
            for (int j = 0; j < 4; j++) {
                switch(j) {
                    case 0:
                        if (!isInteger(((Vector<String>) GUI.getRowData().get(i)).get(0))) {
                            JOptionPane.showMessageDialog(GUI.getContent(), "Track {Nr} must be a number!", "Error!", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        track = "  TRACK " + ((Vector<String>) GUI.getRowData().get(i)).get(0) + " AUDIO" + newLine;
                        break;
                    case 1:
                        if (((Vector<String>) GUI.getRowData().get(i)).get(1).equals("")) {
                            JOptionPane.showMessageDialog(GUI.getContent(), "Empty Artist!", "Error!", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        track = "    PERFORMER \"" + ((Vector<String>) GUI.getRowData().get(i)).get(1) + "\"" + newLine;
                        break;
                    case 2:
                        if (((Vector<String>) GUI.getRowData().get(i)).get(2).equals("")) {
                            JOptionPane.showMessageDialog(GUI.getContent(), "Empty Title!", "Error!", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        track = "    TITLE \"" + ((Vector<String>) GUI.getRowData().get(i)).get(2) + "\"" + newLine;
                        break;
                    case 3:
                        if (!checkIndex(((Vector<String>) GUI.getRowData().get(i)).get(3))) {
                            JOptionPane.showMessageDialog(GUI.getContent(), "Incorrect Index!", "Error!", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        track = "    INDEX 01 " + ((Vector<String>) GUI.getRowData().get(i)).get(3) + "" + newLine;
                        break;
                }
                completeTrack = completeTrack + track;
            }
            cueFile += completeTrack;
            completeTrack = "";
        }
        JFileChooser fileSave = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("Cue Files (*.cue)", "cue");
        fileSave.setAcceptAllFileFilterUsed(false);
        fileSave.addChoosableFileFilter(filter);
        int ret = fileSave.showSaveDialog(GUI.getContent());
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileSave.getSelectedFile();
            if (!checkFileExtension(file.getName(), "cue")) {
                this.saveFile = new File(file.getAbsoluteFile() + ".cue");
            } else {
                this.saveFile = file.getAbsoluteFile();
            }
        } else {
            return;
        }
        if (this.saveFile.exists() || this.saveFile.equals("")) {
            int dialog = JOptionPane.showConfirmDialog(GUI.getContent(), "The file you selected allready exists!" + newLine + "Are you sure you want to overwrite it?", "File Exists!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (dialog == JOptionPane.NO_OPTION) {
                int fc = fileSave.showSaveDialog(GUI.getContent());
                if (fc == JFileChooser.CANCEL_OPTION) {
                    return;
                } else {
                    File file = fileSave.getSelectedFile();
                    if (!checkFileExtension(file.getName(), "cue")) {
                        this.saveFile = new File(file.getAbsoluteFile() + ".cue");
                    } else {
                        this.saveFile = file.getAbsoluteFile();
                    }
                }
            }
        }
        try {
            FileWriter fs = new FileWriter(this.saveFile);
            BufferedWriter out = new BufferedWriter(fs);
            out.write(cueFile);
            out.close();
            JOptionPane.showMessageDialog(GUI.getContent(), "Succesfully saved the file!", "Succes!", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
