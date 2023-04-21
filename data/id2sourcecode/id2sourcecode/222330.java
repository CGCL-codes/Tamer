    private void doReceive() {
        System.out.println("Received Button Pressed");
        String sFileName = ((String) this.remoteFileTable.getSelectedValue());
        if (sFileName.substring(0, 2).equals(" [") && sFileName.substring((sFileName.length() - 1), sFileName.length()).equals("]")) {
            JOptionPane.showMessageDialog(null, (String) "Directory Transfer is not yet available in this version...", "FileTransfer Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (localList.contains(sFileName)) {
            int r = JOptionPane.showConfirmDialog(null, "The file < " + sFileName + " >\n already exists on Local Machine\n Are you sure you want to overwrite it ?", "File Transfer Warning", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.NO_OPTION) return;
        }
        String remoteFileName = this.remoteLocation.getText();
        remoteFileName += ((String) this.remoteFileTable.getSelectedValue()).substring(1);
        String localDestinationPath = this.localLocation.getText() + ((String) this.remoteFileTable.getSelectedValue()).substring(1);
        viewer.rfb.requestRemoteFile(remoteFileName, localDestinationPath);
    }
