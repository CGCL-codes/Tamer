    public static void overwrite(String filename) throws UserCancelledException {
        boolean exists = new File(filename).exists();
        if (!exists) return;
        String question = "A file called \"" + filename + "\"\n" + "already exists; overwrite it with this data?";
        String title = (App.platform.isMac() ? "" : "Already Exists");
        Object choices[] = new Object[] { "Overwrite", "Cancel" };
        int x = JOptionPane.showOptionDialog(null, question, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, null);
        boolean overwrite = (x == 0);
        if (!overwrite) throw new UserCancelledException();
    }
