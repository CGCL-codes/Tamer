    public void perform(ActionEvent e) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.EDIT)) {
            JOptionPane.showMessageDialog(mainForm, MiscUtils.wordWrap(String.format("Up to now lis.to does not yet provide its own " + "preference dialog. However, the application preferences are kept in a simple " + "text file (currently '%s') that you can edit. You have to restart lis.to to make " + "your changes take effect.", filename), 80));
            try {
                Desktop.getDesktop().edit(new File(filename));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainForm, MiscUtils.wordWrap(String.format("lis.to could not open the default text editor " + "for the file '%s'. Please open the file manually (and restart lis.to)!", filename), 80), "Error opening preferences file", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(mainForm, MiscUtils.wordWrap(String.format("lis.to can not open the default text editor " + "on this system. Please edit the file %s manually (and restart lis.to)!", filename), 80));
        }
    }
