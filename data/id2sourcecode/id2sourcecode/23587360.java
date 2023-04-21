    private void save() {
        updateParams(true);
        updateGUI();
        if (api == null) {
            fc = new JFileChooser();
            int choice = fc.showSaveDialog(myself);
            if (choice == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                if (file.exists()) {
                    String msg = "File \"" + file.getName() + "\" already exists: overwrite file?";
                    int ret = JOptionPane.showConfirmDialog(myself, msg, "save file", JOptionPane.YES_NO_OPTION);
                    if (ret != JOptionPane.YES_OPTION) {
                        JOptionPane.showMessageDialog(myself, "Experiment not saved!");
                        return;
                    }
                }
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    PrintStream ps = new PrintStream(fos);
                    ps.println(generateXML());
                    JOptionPane.showMessageDialog(myself, "Experiment succesfully saved!");
                    fos.close();
                } catch (Exception ex) {
                    new DCLXMessage("Saving Error", ex.getMessage());
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(myself, "Experiment not saved!");
            }
        } else {
            api.updateVirtualExp(exp);
            setStatus("Experiment saved");
        }
    }
