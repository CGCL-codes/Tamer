    private JPanel buildPanel() {
        final JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(625, 350));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        JLabel nameLabel = new JLabel(GUIMessages.getString("GUI.context") + " : " + contextName);
        gc.insets = new Insets(20, 20, 5, 20);
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.NONE;
        panel.add(nameLabel, gc);
        JLabel suppLabel = new JLabel(GUIMessages.getString("GUI.minSupport") + " : " + (minSupport * 100.0) + "%");
        gc.insets = new Insets(5, 20, 2, 2);
        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.gridwidth = 1;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.NONE;
        panel.add(suppLabel, gc);
        JLabel confLabel = new JLabel(GUIMessages.getString("GUI.minConfidence") + " : " + (minConfidence * 100.0) + "%");
        gc.insets = new Insets(2, 20, 5, 2);
        gc.gridx = 0;
        gc.gridy = 2;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.gridwidth = 1;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.NONE;
        panel.add(confLabel, gc);
        final JLabel ruleCountLabel = new JLabel(GUIMessages.getString("GUI.ruleCount") + " : " + rules.size());
        gc.insets = new Insets(5, 20, 10, 2);
        gc.gridx = 0;
        gc.gridy = 3;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.gridwidth = 1;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.NONE;
        panel.add(ruleCountLabel, gc);
        reduceRules = new JCheckBox();
        reduceRules.setText("Discard Redundant Exact Rules");
        gc.insets = new Insets(5, 20, 0, 10);
        gc.gridx = 0;
        gc.gridy = 4;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.gridwidth = 1;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.NONE;
        reduceRules.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                isReducedRules = reduceRules.isSelected();
                if (isReducedRules) {
                    Vector<Rule> reducedRules = new Vector<Rule>();
                    reducedRules.clear();
                    reducedRules.addAll(rules);
                    for (Rule rule1 : rules) {
                        for (Rule rule2 : rules) {
                            if (rule2 != rule1 && rule2.getAntecedent().isIncluding(rule1.getAntecedent()) && rule1.getConsequence().equals(rule2.getConsequence()) && rule2.getConfidence() == 1.00 && rule1.getConfidence() == 1.00) {
                                reducedRules.remove(rule2);
                            }
                        }
                    }
                    rules = reducedRules;
                    ruleCountLabel.setText(GUIMessages.getString("GUI.ruleCount") + " : " + rules.size());
                    ruleTable.setRuleTable(rules);
                } else {
                    rules.clear();
                    rules.addAll(oldRules);
                    ruleCountLabel.setText(GUIMessages.getString("GUI.ruleCount") + " : " + rules.size());
                    ruleTable.setRuleTable(rules);
                }
            }
        });
        panel.add(reduceRules, gc);
        ruleTable = new RuleTable(rules, latticePanel);
        ruleScrollPane = new JScrollPane(ruleTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        ruleScrollPane.setBorder(BorderFactory.createEmptyBorder());
        gc.insets = new Insets(10, 20, 5, 20);
        gc.gridx = 0;
        gc.gridy = 5;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.fill = GridBagConstraints.BOTH;
        panel.add(ruleScrollPane, gc);
        xmlButton = new JButton();
        xmlButton.setText(GUIMessages.getString("GUI.xmlexport"));
        gc.insets = new Insets(5, 20, 20, 2);
        gc.gridx = 0;
        gc.gridy = 6;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.gridwidth = 1;
        gc.anchor = GridBagConstraints.SOUTHWEST;
        gc.fill = GridBagConstraints.NONE;
        xmlButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setMultiSelectionEnabled(false);
                chooser.setFileFilter(new XML_Filter(""));
                chooser.setDialogTitle(GUIMessages.getString("GUI.xmlsavedialog"));
                if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    String fileName = chooser.getSelectedFile().getAbsolutePath();
                    if (!fileName.substring(fileName.length() - 4, fileName.length()).equals(".xml")) {
                        fileName += ".xml";
                    }
                    try {
                        File sortie = new File(fileName);
                        if (sortie.exists()) {
                            int overwrite = DialogBox.showDialogWarning(panel, GUIMessages.getString("GUI.doYouWantToOverwriteFile"), GUIMessages.getString("GUI.selectedFileAlreadyExist"));
                            if (overwrite == DialogBox.NO) {
                                DialogBox.showMessageInformation(panel, GUIMessages.getString("GUI.contextHasNotBeenSaved"), GUIMessages.getString("GUI.notSaved"));
                            } else if (overwrite == DialogBox.YES) {
                                @SuppressWarnings("unused") RulesWriter exporter = new RulesWriter(sortie, rules, minSupport, minConfidence, contextName, panel);
                            }
                        } else {
                            @SuppressWarnings("unused") RulesWriter exporter = new RulesWriter(sortie, rules, minSupport, minConfidence, contextName, panel);
                        }
                    } catch (NoClassDefFoundError e1) {
                        JOptionPane.showMessageDialog(panel, GUIMessages.getString("GUI.jdomLibraryMissing"), GUIMessages.getString("GUI.notSaved"), JOptionPane.ERROR_MESSAGE);
                    } catch (IOException e1) {
                        System.err.println("I/O Error");
                    }
                }
            }
        });
        panel.add(xmlButton, gc);
        return panel;
    }
