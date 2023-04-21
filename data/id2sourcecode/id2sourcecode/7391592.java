    public void exportTable(int format) {
        Map<Integer, String> fileEndings = new HashMap<Integer, String>(8);
        fileEndings.put(0, ".txt");
        fileEndings.put(1, ".csv");
        fileEndings.put(2, ".txt");
        if (table != null && table.getRowCount() > 0) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Export file as " + fileEndings.get(format));
            fc.setDialogType(JFileChooser.SAVE_DIALOG);
            int state = fc.showSaveDialog(this);
            if (state == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                if (!file.getName().endsWith(fileEndings.get(format))) {
                    file = new File(file.getAbsolutePath() + fileEndings.get(format));
                }
                try {
                    if (!file.exists()) {
                        menuBar.getPictureBox().saveToFile(file, format);
                    } else {
                        int answer = JOptionPane.showConfirmDialog(this, "The file '" + file.getName() + "' does already exist - overwrite it?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (answer == JOptionPane.YES_OPTION) {
                            menuBar.getPictureBox().saveToFile(file, format);
                        }
                        if (answer == JOptionPane.NO_OPTION) {
                            exportTable(format);
                        }
                    }
                } catch (IOException ioe) {
                    logger.log(Level.WARNING, "error while exporting file with format " + fileEndings.get(format), ioe);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "There are no data that could be exported.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
