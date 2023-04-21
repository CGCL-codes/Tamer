    public void writeWorld() {
        try {
            pushPaused(true);
            showMessage("Saving");
            if (fileChooser.showSaveDialog(this) == APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.toString().toLowerCase().endsWith("." + FILE_EXTENSION)) {
                    file = new File(file + "." + FILE_EXTENSION);
                }
                if (file.exists()) {
                    String OverwriteOption = "Overwrite";
                    String CancelOption = "Cancel";
                    Object[] possibleValues = { OverwriteOption, CancelOption };
                    int n = JOptionPane.showOptionDialog(this, file.getName() + " already exists in this directory.  Should it be overwritten?", "Overwrite?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, possibleValues, OverwriteOption);
                    if (n == 0) ImageIO.write(world, "png", file);
                } else ImageIO.write(world, "png", file);
            }
            popPaused();
            forcePaint = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
