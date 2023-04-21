    public void saveFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new XMLFilter());
        chooser.setCurrentDirectory(new File("D:/Programme/EPFL/Webots/controllers/Judoka0"));
        int returnVal = chooser.showOpenDialog(motionInterface);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String fileName = chooser.getSelectedFile().getAbsolutePath();
            System.out.println("You chose to open this file: " + fileName);
            try {
                FileReader File = new FileReader(fileName);
                Object[] options = { "Overwrite", "Cancel" };
                int cancel = JOptionPane.showOptionDialog(motionInterface, "The file you have chosen already exists, do you want to overwrite it?", "File already Exists", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (cancel == 0) {
                    motionInterface.saveBoardToFile(fileName);
                }
            } catch (IOException ex) {
                System.out.println("File doesn't exist creating it.. ");
                motionInterface.saveBoardToFile(fileName);
            }
        }
    }
