    public void actionPerformed(ActionEvent aEvent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("New Archive");
        NeonFileFilter filter = new NeonFileFilter();
        filter.addExtension("zip");
        filter.setDescription("ZIP " + "Archives");
        chooser.setFileFilter(filter);
        JPanel accessory = new JPanel();
        JCheckBox checkbox = new JCheckBox("Add Dialog");
        accessory.add(checkbox);
        chooser.setAccessory(accessory);
        int result = chooser.showDialog(fMain, "New Archive");
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (file.exists()) {
                int response = JOptionPane.showConfirmDialog(fMain, "File " + file.toString() + " already exists!\n " + "Do you want to overwrite existing file?", fMain.TITLE, JOptionPane.OK_CANCEL_OPTION);
                if (response != JOptionPane.OK_OPTION) return;
            }
            fMain.wakeupPlugin(file);
            PluginInterface plugin = fMain.getPlugin();
            if (plugin == null) {
                Utils.sayWarning("NeonZip don't support selected file type: " + file.toString(), fMain);
                return;
            }
            plugin.createNew(file);
            fMain.setTitle(Main.TITLE + " - " + Utils.removePath(file.toString()));
            if (checkbox.isSelected()) {
                new AddAction(fMain).actionPerformed(new ActionEvent(this, 0, "invoke addactiom"));
            }
        }
    }
