    public void doBackup() {
        JFileChooser fc = new JFileChooser();
        File lastBackup = new File(Configuration.getGlobal(BACKUPFILE_KEY, BACKUPFILE_DEF));
        fc.setSelectedFile(lastBackup);
        fc.setCurrentDirectory(lastBackup.getParentFile());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int ch = fc.showSaveDialog(gui);
        if (ch != JFileChooser.APPROVE_OPTION) {
            logger.info("Backup canceled by user");
            return;
        }
        File sf = fc.getSelectedFile();
        if (!sf.exists() || !sf.canWrite() || !sf.isDirectory()) {
            logger.error("Wrong folder " + sf.getAbsolutePath());
            JOptionPane.showMessageDialog(gui, Language.string("Unable to write to the specified folder"), Language.string("Write error"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        String filename = backupNameFormat.format(new Object[] { DatePicker.getTimestamp(new Date()) });
        sf = new File(sf.getAbsoluteFile() + File.separator + filename);
        if (sf.exists()) {
            ch = JOptionPane.showConfirmDialog(gui, Language.string("A file with the same name already exists: are you sure you want to overwrite?"), Language.string("Overwrite?"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ch != JOptionPane.YES_OPTION) {
                logger.info("Backup overwrite aborted by user");
                return;
            }
        }
        boolean created = false;
        try {
            created = sf.createNewFile();
        } catch (Exception e1) {
            logger.error("Creating new dump file " + sf.getAbsolutePath(), e1);
        }
        if (!created) {
            logger.error("Cannot create file " + sf.getAbsolutePath());
            JOptionPane.showMessageDialog(gui, Language.string("Unable to create the target file"), Language.string("Write error"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        Configuration.getGlobalConfiguration().setProperty(BACKUPFILE_KEY, sf.getAbsolutePath());
        try {
            database.dump(sf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
