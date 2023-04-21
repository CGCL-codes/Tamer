    private boolean save(Runnable runner, String filename) {
        File f = new File(filename);
        if (f.exists()) {
            int option = JOptionPane.showConfirmDialog(ApplicationFrame.INSTANCE.mainFrame(), "File " + filename + " already exists, overwrite ?", "Confirmation Required!", JOptionPane.YES_NO_CANCEL_OPTION);
            if (option == JOptionPane.CANCEL_OPTION) {
                return false;
            } else if (option == JOptionPane.NO_OPTION) {
                return true;
            }
        }
        runner.run();
        return true;
    }
