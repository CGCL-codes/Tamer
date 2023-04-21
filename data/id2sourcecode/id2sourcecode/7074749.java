    private static void createCardPanel() {
        cardPanel = new Panel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
        Button b;
        Label label;
        Panel p;
        p = new Panel();
        AWTGridBagContainer grid = new AWTGridBagContainer(p);
        grid.getConstraints().fill = GridBagConstraints.BOTH;
        descText = new TextArea(keyGenerationHelp, 12, 34, TextArea.SCROLLBARS_VERTICAL_ONLY);
        descText.setEditable(false);
        grid.add(descText, 2, 8);
        descText.addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseMoved(MouseEvent e) {
                if (progBar.isFinished()) {
                    try {
                        client.randomSeed().removeProgress();
                        progBar.setValue(0);
                        int bits = Integer.valueOf(choiceBits.getSelectedItem()).intValue();
                        String alg = choiceType.getSelectedItem().substring(0, 3);
                        descText.setText("Generating keypair, please wait...");
                        Thread.yield();
                        KeyPair kp = generateKeyPair(alg, bits);
                        saveKeyPair(kp);
                        okBut.setEnabled(true);
                        okBut.setLabel("Back");
                        descText.setText(keyGenerationComplete);
                        generatedAndSaved = true;
                    } catch (Throwable t) {
                        alert("Error while generating/saving key pair: " + t.getMessage());
                        cardLayout.show(cardPanel, "first");
                    }
                }
            }
        });
        grid.getConstraints().fill = GridBagConstraints.NONE;
        grid.getConstraints().anchor = GridBagConstraints.CENTER;
        progBar = new ProgressBar(512, 150, 20);
        grid.add(progBar, 3, 8);
        cardPanel.add(p, "second");
        p = new Panel();
        grid = new AWTGridBagContainer(p);
        label = new Label("Key type/format:");
        grid.add(label, 0, 2);
        choiceType = AWTConvenience.newChoice(new String[] { "DSA (ssh2)", "RSA (ssh2)", "RSA (ssh1)" });
        grid.add(choiceType, 0, 2);
        label = new Label("Key length (bits):");
        grid.add(label, 1, 2);
        choiceBits = AWTConvenience.newChoice(new String[] { "768", "1024", "1536" });
        grid.add(choiceBits, 1, 2);
        label = new Label("Identity file:");
        grid.add(label, 2, 2);
        fileText = new TextField("", 18);
        grid.add(fileText, 2, 2);
        b = new Button("...");
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                keyGenFD.setVisible(true);
                if (keyGenFD.getFile() != null && keyGenFD.getFile().length() > 0) fileText.setText(keyGenFD.getDirectory() + keyGenFD.getFile());
            }
        });
        grid.getConstraints().fill = GridBagConstraints.NONE;
        grid.add(b, 2, 1);
        grid.getConstraints().fill = GridBagConstraints.HORIZONTAL;
        label = new Label("Password:");
        grid.add(label, 3, 2);
        pwdText = new TextField("", 18);
        pwdText.setEchoChar('*');
        grid.add(pwdText, 3, 2);
        label = new Label("Password again:");
        grid.add(label, 4, 2);
        pwdText2 = new TextField("", 18);
        pwdText2.setEchoChar('*');
        grid.add(pwdText2, 4, 2);
        label = new Label("Comment:");
        grid.add(label, 5, 2);
        commText = new TextField("", 18);
        grid.add(commText, 5, 2);
        cbOpenSSH = new Checkbox("OpenSSH .pub format");
        grid.add(cbOpenSSH, 6, 4);
        cardPanel.add(p, "first");
    }
