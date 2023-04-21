    private void exportXenaFile() {
        JFileChooser chooser = new JFileChooser() {

            static final long serialVersionUID = 1L;

            @Override
            public void approveSelection() {
                if (getSelectedFile().exists() && getSelectedFile().isDirectory()) {
                    super.approveSelection();
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a valid output directory.", "Invalid Output Directory", JOptionPane.WARNING_MESSAGE, IconFactory.getIconByName("images/icons/warning_32.png"));
                }
            }
        };
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setDialogTitle("Choose Export Directory");
        int retVal = chooser.showSaveDialog(this);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            Xena xena = viewManager.getPluginManager().getXena();
            try {
                this.setCursor(busyCursor);
                xena.export(new XenaInputSource(xenaFile), chooser.getSelectedFile());
                this.setCursor(defaultCursor);
                JOptionPane.showMessageDialog(this, "Xena file exported successfully.", "Export Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (FileExistsException e) {
                this.setCursor(defaultCursor);
                retVal = JOptionPane.showConfirmDialog(this, "A file with the same name already exists in this directory. Do you want to overwrite it?", "File Already Exists", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (retVal == JOptionPane.OK_OPTION) {
                    try {
                        this.setCursor(busyCursor);
                        xena.export(new XenaInputSource(xenaFile), chooser.getCurrentDirectory(), true);
                        this.setCursor(defaultCursor);
                    } catch (Exception ex) {
                        handleException(ex);
                    }
                }
            } catch (Exception e) {
                handleException(e);
            } finally {
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }
