    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(parentFrame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String path = file.getAbsolutePath();
            if (!path.endsWith(".xml")) {
                path += ".xml";
            }
            File ff = new File(path);
            boolean canSave = false;
            if (!ff.exists()) {
                try {
                    ff.createNewFile();
                    canSave = true;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else {
                int i = JOptionPane.showConfirmDialog(parentFrame, "This file already exist. Do you want to overwrite?", "File already exist", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                canSave = i == JOptionPane.OK_OPTION;
            }
            if (canSave) facade.saveTasks(ff);
        }
    }
