    public void btnExportActionPerformed(ActionEvent e) {
        String msg;
        String title;
        if (!chkExportToClipboard.isSelected() && !chkExportToFile.isSelected()) {
            msg = "You should choose at least one export mode :\n" + "-export to file\n" + "-export to clipboard";
            title = "Hey !!!";
            if (parentInternalFrame != null) JOptionPane.showInternalMessageDialog(parentInternalFrame.getDesktopPane(), msg, title, JOptionPane.ERROR_MESSAGE); else JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (chkCellSeparator.isSelected() && txtCellSeparator.getText().equals("")) {
            msg = "Empty cell separator !";
            title = "Cannot export !";
            if (parentInternalFrame != null) JOptionPane.showInternalMessageDialog(parentInternalFrame.getDesktopPane(), msg, title, JOptionPane.ERROR_MESSAGE); else JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (chkRowSeparator.isSelected() && txtRowSeparator.getText().equals("")) {
            msg = "Empty row separator !";
            title = "Cannot export !";
            if (parentInternalFrame != null) JOptionPane.showInternalMessageDialog(parentInternalFrame.getDesktopPane(), msg, title, JOptionPane.ERROR_MESSAGE); else JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (chkCellSeparator.isSelected() && chkRowSeparator.isSelected() && txtCellSeparator.getText().equals(chkRowSeparator.getText())) {
            msg = "Row and Cell separators are identical !";
            title = "Ohoh...";
            if (parentInternalFrame != null) JOptionPane.showInternalMessageDialog(parentInternalFrame.getDesktopPane(), msg, title, JOptionPane.ERROR_MESSAGE); else JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (chkExportToFile.isSelected() && txtFileName.getText().equals("")) {
            msg = "You should select a file using the browse button...";
            title = "Hey !!!!";
            if (parentInternalFrame != null) JOptionPane.showInternalMessageDialog(parentInternalFrame.getDesktopPane(), msg, title, JOptionPane.ERROR_MESSAGE); else JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
            return;
        }
        File fichierExport = new File(txtFileName.getText());
        if (fichierExport.exists() && chkExportToFile.isSelected()) {
            msg = "The file you specifed already exists.\n" + "Do you want to overwrite it ?";
            title = "Hey !!!";
            int retValue;
            if (parentInternalFrame != null) retValue = JOptionPane.showInternalConfirmDialog(parentInternalFrame.getDesktopPane(), msg, title, JOptionPane.YES_NO_OPTION); else retValue = JOptionPane.showConfirmDialog(this, msg, title, JOptionPane.YES_NO_OPTION);
            if (retValue != JOptionPane.YES_OPTION) return;
        }
        TableExporterThread thread = new TableExporterThread(this, tableToExport);
        progressBar.setValue(0);
        int nbreCellules = tableToExport.getRowCount() * tableToExport.getColumnCount();
        if (chkExportToClipboard.isSelected() && chkExportToFile.isSelected()) progressBar.setMaximum(nbreCellules * 2); else progressBar.setMaximum(nbreCellules);
        cancelAsked = false;
        thread.start();
    }
