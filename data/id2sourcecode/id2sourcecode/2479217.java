    private void newPortableButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("windows") > 0) {
            JOptionPane.showMessageDialog(this, "Sorry, indexing of portable devices is currenly supported\n" + "only in MS Windows operating systems.", "Invalid operation", JOptionPane.ERROR_MESSAGE, this.imageControl.getErrorIcon());
            return;
        }
        File[] roots = File.listRoots();
        File root = (File) JOptionPane.showInputDialog(this, "Drive letter:", "Select device to index", JOptionPane.QUESTION_MESSAGE, this.imageControl.getQuestionIcon(), roots, new JComboBox());
        if (root == null) {
            return;
        }
        File index = null;
        try {
            index = new File(root.getCanonicalPath() + File.separator + ".puggle");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (index == null) {
            return;
        } else if (index.exists()) {
            Object[] options = { "Open Index", "Overwrite Index" };
            int opt = JOptionPane.showOptionDialog(this, "Index directory '" + index + "' already exists.\n" + "Do you wish to open existing Index or Overwrite?\n" + "(Choosing to overwrite will delete existing Index)", "Error Creating Index Directory", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
            if (opt == JOptionPane.NO_OPTION) {
                if (Util.deleteDir(index) == false) {
                    JOptionPane.showMessageDialog(this, "Directory '" + index + "' cannot be deleted.", "Error Creating Index Directory", JOptionPane.ERROR_MESSAGE, this.imageControl.getErrorIcon());
                    return;
                }
                if (index.mkdir() == false) {
                    JOptionPane.showMessageDialog(this, "Cannot create directory '" + index.getAbsolutePath() + "'.", "Error Creating Index Directory", JOptionPane.ERROR_MESSAGE, this.imageControl.getErrorIcon());
                    return;
                }
            }
        }
        this.close();
        try {
            Resources.setIndex(index);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.init();
        this.indexProperties.setFilesystemRoot(root.getAbsolutePath());
        this.indexProperties.setPath(root);
        this.indexProperties.setPortable(true);
        this.indexPropertiesPanel.setProperties(this.indexProperties);
    }
