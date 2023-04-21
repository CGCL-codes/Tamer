    private void doSend() {
        System.out.println("Send Button Pressed");
        String sFileName = ((String) this.localFileTable.getSelectedValue());
        if (sFileName.substring(0, 2).equals(" [") && sFileName.substring((sFileName.length() - 1), sFileName.length()).equals("]")) {
            JOptionPane.showMessageDialog(null, (String) "Directory Transfer is not yet available in this version...", "FileTransfer Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (remoteList.contains(sFileName)) {
            int r = JOptionPane.showConfirmDialog(null, "The file < " + sFileName + " >\n already exists on Remote Machine\n Are you sure you want to overwrite it ?", "File Transfer Warning", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.NO_OPTION) return;
        }
        String source = this.localLocation.getText();
        source += ((String) this.localFileTable.getSelectedValue()).substring(1);
        String destinationPath = this.remoteLocation.getText();
        viewer.rfb.offerLocalFile(source, destinationPath);
    }
