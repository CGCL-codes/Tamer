    void saveas_actionPerformed(ActionEvent e) {
        try {
            JFileChooser jfc = null;
            if (currentFileFolder == null) {
                jfc = new JFileChooser();
            } else {
                jfc = new JFileChooser(this.currentFileFolder);
                jfc.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {

                    public boolean accept(File f) {
                        boolean acceptedFormat = f.getName().toLowerCase().endsWith(".xml");
                        return acceptedFormat || f.isDirectory();
                    }

                    public String getDescription() {
                        return "xml";
                    }
                });
            }
            boolean invalidFolder = true;
            File sel = null;
            while (invalidFolder) {
                jfc.setLocation(getCenter(jfc.getSize()));
                jfc.showSaveDialog(this);
                sel = jfc.getSelectedFile();
                invalidFolder = sel != null && !sel.getParentFile().exists();
                if (invalidFolder) {
                    JOptionPane.showMessageDialog(this, "You cannot save your file to " + sel.getParentFile().getPath() + ". That folder does not exist. Please, try again", "Error", JOptionPane.WARNING_MESSAGE);
                }
            }
            if (sel != null && !sel.isDirectory()) {
                if (sel.exists()) {
                    int result = JOptionPane.showConfirmDialog(this, "The file already exists. Do you want to overwrite (y/n)?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        PersistenceManager p = new PersistenceManager();
                        p.save(sel, ids);
                        this.currentFile = sel;
                        this.currentFileFolder = sel.getParentFile();
                        this.updateHistory(currentFile);
                        setTitle("Project:" + sel.getAbsolutePath());
                        setUnChanged();
                    }
                } else {
                    PersistenceManager p = new PersistenceManager();
                    if (!sel.getPath().toLowerCase().endsWith(".xml")) {
                        sel = new File(sel.getPath() + ".xml");
                    }
                    p.save(sel, ids);
                    this.currentFile = sel;
                    this.currentFileFolder = sel.getParentFile();
                    setTitle("Project:" + sel.getAbsolutePath());
                    this.updateHistory(currentFile);
                    setUnChanged();
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
