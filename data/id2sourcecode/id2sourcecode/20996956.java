            public void actionPerformed(ActionEvent event) {
                Object[] options = { "Save", "Cancel" };
                int a = JOptionPane.showOptionDialog(null, testCase, "Generate Test Case", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("./misc/fighter.png"), options, options[0]);
                if (a == JOptionPane.OK_OPTION) {
                    testCase.saveSrc();
                    File f = new File(fwSaveDir + "TestCase.java");
                    if (f.exists()) {
                        a = JOptionPane.showConfirmDialog(null, new JLabel("A test case already exists. Overwrite?"), "Confirm overwrite", JOptionPane.OK_CANCEL_OPTION);
                        if (a == JOptionPane.CANCEL_OPTION) return;
                    }
                    executeCode.setEnabled(true);
                    Thread t = new Thread(new JavaCodeWriter(true, testCase.getSrc(), fwSaveDir + "TestCase.java", null));
                    t.start();
                } else {
                    testCase.clearTextArea();
                }
            }
