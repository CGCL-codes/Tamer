    public boolean saveMessagesAs(Hl7V2MessageCollection theSelectedValue) {
        Validate.notNull(theSelectedValue);
        if (mySaveMessagesFileChooser == null) {
            mySaveMessagesFileChooser = new JFileChooser(Prefs.getSavePathMessages());
            mySaveMessagesFileChooser.setDialogTitle("Choose a file to save the current message(s) to");
            mySaveMessagesFileChooserAccessory = new FileChooserSaveAccessory();
            mySaveMessagesFileChooser.setAccessory(mySaveMessagesFileChooserAccessory);
            FileFilter type = new ExtensionFilter("HL7 Files", new String[] { ".hl7" });
            mySaveMessagesFileChooser.addChoosableFileFilter(type);
            type = new ExtensionFilter("XML Files", new String[] { ".xml" });
            mySaveMessagesFileChooser.addChoosableFileFilter(type);
            type = new AllFileFilter();
            mySaveMessagesFileChooser.addChoosableFileFilter(type);
            mySaveMessagesFileChooser.setPreferredSize(new Dimension(700, 500));
        }
        int value = mySaveMessagesFileChooser.showSaveDialog(myView.getMyframe());
        if (value == JFileChooser.APPROVE_OPTION) {
            File file = mySaveMessagesFileChooser.getSelectedFile();
            Prefs.setSavePathMessages(file.getPath());
            if (!file.getName().contains(".")) {
                switch(theSelectedValue.getEncoding()) {
                    case ER_7:
                        file = new File(file.getAbsolutePath() + ".hl7");
                        break;
                    case XML:
                        file = new File(file.getAbsolutePath() + ".xml");
                        break;
                }
            }
            if (file.exists()) {
                String message = "The file \"" + file.getName() + "\" already exists. Do you wish to overwrite it?";
                int confirmed = showDialogYesNo(message);
                if (confirmed == JOptionPane.NO_OPTION) {
                    return false;
                }
                ourLog.info("Deleting file: {}", file.getAbsolutePath());
                file.delete();
            }
            theSelectedValue.setSaveCharset(mySaveMessagesFileChooserAccessory.getSelectedCharset());
            theSelectedValue.setSaveFileName(file.getAbsolutePath());
            theSelectedValue.setSaveStripComments(mySaveMessagesFileChooserAccessory.isSelectedSaveStripComments());
            theSelectedValue.setSaveLineEndings(mySaveMessagesFileChooserAccessory.getSelectedLineEndings());
            doSave(theSelectedValue);
            updateRecentMessageFiles();
            return true;
        } else {
            return false;
        }
    }
