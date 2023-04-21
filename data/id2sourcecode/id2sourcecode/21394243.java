    void saveGenBankFile() {
        JFileChooser fileChooser = null;
        String preferredDir = theQTPAnalyzerFrame.getSettings().saveDir;
        if (preferredDir != null) {
            fileChooser = new JFileChooser(preferredDir);
        }
        TasselFileFilter fileFilter = new TasselFileFilter();
        fileFilter.addExtension("phy");
        fileFilter.setDescription("Phylip Format");
        fileChooser.setFileFilter(fileFilter);
        int userChoice = fileChooser.showSaveDialog(theQTPAnalyzerFrame);
        if (userChoice == JFileChooser.APPROVE_OPTION) {
            File aFile = fileChooser.getSelectedFile();
            boolean notUniquelyNamed = true;
            while (notUniquelyNamed) {
                if (aFile.exists()) {
                    int returnVal = JOptionPane.showOptionDialog(theQTPAnalyzerFrame, "This file already exists.  " + "Do you wish to overwrite it?", "Overwrite File Confirmation", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
                    if (returnVal == JOptionPane.CANCEL_OPTION) {
                        return;
                    }
                    if (returnVal == JOptionPane.NO_OPTION) {
                        fileChooser.showSaveDialog(theQTPAnalyzerFrame);
                    }
                    if (returnVal == JOptionPane.YES_OPTION) {
                        notUniquelyNamed = false;
                        try {
                            DataOutputStream dos = new DataOutputStream(new FileOutputStream(aFile));
                            dos.writeBytes("" + sb);
                            dos.flush();
                            dos.close();
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                    }
                }
            }
        }
    }
