    public static File saveAs(Model model) {
        File modelsFile = (File) model.getPropertyValue(Model.PROPERTY_FILE);
        FileChooser fc = FileChooser.getDataInstance();
        int result = fc.showSaveDialog(JVibes.getFrame());
        if (result == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            if (!f.getAbsolutePath().toLowerCase().endsWith(".jvm")) f = new File(f.getAbsolutePath() + ".jvm");
            if (f.exists() && !f.equals(modelsFile)) {
                result = JOptionPane.showConfirmDialog(JVibes.getFrame(), "File already exists. Overwrite?");
                if (result != JOptionPane.OK_OPTION) return null;
            }
            save(model, f);
            return f;
        }
        return null;
    }
