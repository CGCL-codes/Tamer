    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("ok") || e.getSource() == bapString) {
            if (!sha1.isSelected()) {
                if (bapString.getText().length() == 0) {
                    bapValue = null;
                } else {
                    bapValue = bapString.getText().getBytes();
                }
            } else {
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA1");
                    byte[] t = md.digest(bapString.getText().getBytes());
                    bapValue = new byte[16];
                    System.arraycopy(t, 0, bapValue, 0, 16);
                } catch (NoSuchAlgorithmException nsae) {
                }
            }
            dispose();
        }
        if (e.getActionCommand().equals("cancel")) {
            bapValue = null;
            dispose();
        }
        if (e.getActionCommand().equals("load")) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setFileFilter(new FileFilter() {

                public boolean accept(File f) {
                    return f.isDirectory();
                }

                public String getDescription() {
                    return "Directories";
                }
            });
            int choice = fileChooser.showOpenDialog(this);
            switch(choice) {
                case JFileChooser.APPROVE_OPTION:
                    try {
                        File file = fileChooser.getSelectedFile();
                        TerminalCVCertificateDirectory.getInstance().scanDirectory(file);
                    } catch (IOException ioe) {
                    }
                    break;
                default:
                    break;
            }
        }
    }
