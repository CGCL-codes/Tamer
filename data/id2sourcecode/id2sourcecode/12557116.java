    private void bnLocateFileActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.home")));
        chooser.setFileFilter(new FileFilter() {

            public String getDescription() {
                return ".csv";
            }

            @Override
            public boolean accept(File file) {
                boolean status = false;
                try {
                    String fileName = file.getName().toLowerCase();
                    status = fileName.endsWith(".csv");
                } catch (Exception e) {
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
                    createFile = new File(chooser.getSelectedFile().toString() + ".csv");
                    if (createFile.exists()) {
                        int cnt = JOptionPane.showConfirmDialog(this, "This file already exists ! Are you sure \n you want to over write it.", "check", JOptionPane.OK_CANCEL_OPTION);
                        if (cnt == 0) {
                            tfGenerateFile.setText(createFile.toString());
                        } else {
                            createFile = null;
                            tfGenerateFile.setText("");
                        }
                    } else {
                        tfGenerateFile.setText(createFile.toString());
                    }
                } else {
                    str.nextToken();
                    String s1 = str.nextToken(".");
                    if (s1.equalsIgnoreCase("csv")) {
                        createFile = new File(chooser.getSelectedFile().toString());
                        if (createFile.exists()) {
                            int cnt = JOptionPane.showConfirmDialog(this, "This file already exists ! Are you sure \n you want to over write it.", "check", JOptionPane.OK_CANCEL_OPTION);
                            if (cnt == 0) {
                                tfGenerateFile.setText(createFile.toString());
                            } else {
                                createFile = null;
                                tfGenerateFile.setText("");
                            }
                        } else {
                            tfGenerateFile.setText(createFile.toString());
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "The given file is not in .csv format \n Please create .csv extension.", "check", JOptionPane.CANCEL_OPTION);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "The given file name is not correct \n Please create a new file.", "check", JOptionPane.YES_OPTION);
            }
        } else {
            tfGenerateFile.setText("");
        }
        try {
            if (createFile != null) {
                OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(tfGenerateFile.getText()), "UTF8");
                osw.flush();
                osw.close();
                jProgressBar1.setString("File is created.");
            } else {
                JOptionPane.showMessageDialog(null, "Please select the file to generate the data.", "check", JOptionPane.YES_OPTION);
            }
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }
