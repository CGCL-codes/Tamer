    private boolean checkFileOverwrite(final boolean status, final boolean already) {
        boolean rval = status;
        if (status && already) {
            final int optionPaneRely = JOptionPane.showConfirmDialog(this.frame, "Overwrite existing files? \n", "File Exists", JOptionPane.YES_NO_OPTION);
            if (optionPaneRely == JOptionPane.NO_OPTION) {
                rval = false;
            }
        }
        return rval;
    }
