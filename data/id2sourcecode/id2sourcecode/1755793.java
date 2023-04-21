    private boolean overwrite(File file) {
        boolean overwritePreference = (Registry.getPreference(Tokens.SILENTOVERWRITE) != null) ? new Boolean(Registry.getPreference(Tokens.SILENTOVERWRITE)).booleanValue() : false;
        if (overwritePreference) {
            return true;
        } else {
            if (file.exists()) {
                Object[] options = { "Overwrite", "Cancel" };
                int response = JOptionPane.showOptionDialog((Component) Registry.getAttribute(Tokens.APPLICATION), "The file : " + file.getAbsolutePath() + " \nalready exists.\nDo you want to overwrite it?", "Confirm overwrite", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                if (response == JOptionPane.YES_OPTION) {
                    return true;
                }
            }
        }
        return false;
    }
