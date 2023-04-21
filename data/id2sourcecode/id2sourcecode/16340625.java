    public void actionPerformed(ActionEvent e) {
        if (ChannelEditor.application.getChannelFile() != null && ChannelEditor.application.isModified()) {
            File saveFile = ChannelEditor.application.getChannelFile();
            try {
                FileWriter outFile = new FileWriter(saveFile);
                Utils.outputChannelTree(outFile, ChannelEditor.application.getChannelListingPanel().getRootNode());
                ChannelEditor.application.setModified(false);
            } catch (Exception ioe) {
                JOptionPane.showConfirmDialog(ChannelEditor.application, Messages.getString("SaveAction.2") + saveFile.getPath() + Messages.getString("SaveAction.3") + ioe.getMessage(), Messages.getString("SaveAction.4"), JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                ioe.printStackTrace();
            }
        } else {
            if (ChannelEditor.application.getChannelFile() == null) {
                ActionManager.getInstance().getSaveAsAction().actionPerformed(e);
            }
        }
    }
