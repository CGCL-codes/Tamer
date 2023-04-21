    private void jBrowseButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.home")));
        chooser.setFileFilter(new FileFilter() {

            public String getDescription() {
                return ".txt";
            }

            public boolean accept(File file) {
                boolean status = false;
                try {
                    String fileName = file.getName().toLowerCase();
                    status = fileName.endsWith(".txt");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return status;
            }
        });
        int i = chooser.showSaveDialog(this);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (i == JFileChooser.APPROVE_OPTION) {
            String file = chooser.getSelectedFile().toString();
            StringTokenizer str = new StringTokenizer(file, ".");
            if (str.countTokens() <= 2) {
                if (str.countTokens() == 1) {
                    createFile = new File(chooser.getSelectedFile().toString() + ".txt");
                    if (createFile.exists()) {
                        int cnt = JOptionPane.showConfirmDialog(this, "This file already exists ! Are you sure \n you want to over write it.", "check", JOptionPane.OK_CANCEL_OPTION);
                        if (cnt == 0) {
                            jTextField6.setText(createFile.toString());
                            System.out.println("override");
                        } else {
                            createFile = null;
                            jTextField6.setText("");
                        }
                    } else {
                        jTextField6.setText(createFile.toString());
                    }
                } else {
                    str.nextToken();
                    String s1 = str.nextToken(".");
                    if (s1.equalsIgnoreCase("txt")) {
                        createFile = new File(chooser.getSelectedFile().toString());
                        if (createFile.exists()) {
                            int cnt = JOptionPane.showConfirmDialog(this, "This file already exists ! Are you sure \n you want to over write it.", "check", JOptionPane.OK_CANCEL_OPTION);
                            if (cnt == 0) {
                                jTextField6.setText(createFile.toString());
                                System.out.println("override");
                            } else {
                                createFile = null;
                                jTextField6.setText("");
                            }
                        } else {
                            jTextField6.setText(createFile.toString());
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "The given file is not in .txt format \n Please create .csv extension.", "check", JOptionPane.CANCEL_OPTION);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "The given file name is not correct \n Please create a new file.", "check", JOptionPane.YES_OPTION);
            }
        } else {
            jTextField6.setText("");
        }
        try {
            if (createFile != null) {
                OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(jTextField6.getText()));
                osw.flush();
                osw.close();
            } else {
                JOptionPane.showMessageDialog(null, "Please select the file to export the data.", "check", JOptionPane.YES_OPTION);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
