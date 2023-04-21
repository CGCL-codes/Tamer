    private void okAction() {
        if (validateDataEntry()) {
            int conf = JOptionPane.YES_OPTION;
            int overwrite = -1;
            String name = getDriverName();
            overwrite = findDriver(name);
            if (overwrite >= 0) {
                conf = JOptionPane.showConfirmDialog(this, "Overwrite " + name + " driver?", "Driver already exists", JOptionPane.YES_NO_CANCEL_OPTION);
            }
            if (conf == JOptionPane.YES_OPTION) {
                addToList(overwrite);
                writeProfiles();
            }
            if (conf != JOptionPane.CANCEL_OPTION) {
                exitValue = DBDriverDialog.OK;
                setVisible(false);
            }
        }
    }
