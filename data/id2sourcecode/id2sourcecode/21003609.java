    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = makeFileChooser();
        if (JFileChooser.APPROVE_OPTION == fc.showSaveDialog(null)) {
            File f = fc.getSelectedFile();
            if (f != null) {
                try {
                    if (f.canWrite()) {
                        if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(sourceToComponent(e), "File already exists. Overwrite?")) {
                            return;
                        }
                    } else {
                        String fname = f.getName();
                        int pos = fname.lastIndexOf('.');
                        if (pos == -1) {
                            f = new File(f + ".iitc");
                        }
                        f.createNewFile();
                    }
                    FileOutputStream out = new FileOutputStream(f);
                    _ic.getState().store(out, "IIT Contour File");
                } catch (IOException e2) {
                    JOptionPane.showMessageDialog(sourceToComponent(e), e2.getMessage(), e2.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                    ;
                }
            }
        }
    }
