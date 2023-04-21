    public final boolean writeLevelToFile(final String name) {
        final File dir = new File("levels");
        dir.mkdir();
        final File file = new File(dir + "/" + name + ".jark");
        try {
            if (file.createNewFile()) {
                writeToFile(file);
                return true;
            } else {
                if (JOptionPane.showConfirmDialog(null, "File " + name + " already exists, overwrite ?", "File already exists", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0) {
                    writeToFile(file);
                    return true;
                }
            }
        } catch (final IOException ex) {
            System.out.println(ex);
        }
        return false;
    }
