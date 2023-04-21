    private void startItUp() {
        tree.expandPath(new TreePath(tree.getModel().getRoot()));
        tree.setBackground(scrollPane.getBackground());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(signalCellRenderer);
        scrollPane.setViewportView(tree);
        jText.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectedPVName = jText.getText();
                if (actionListenerProxy != null) {
                    actionListenerProxy.actionPerformed(actionEvent);
                }
            }
        });
        MouseListener ml = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                int selRow = tree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                if (selRow != -1) {
                    if (e.getClickCount() == 1) {
                        jText.setText(null);
                        selectedPVName = null;
                        Object value = selPath.getLastPathComponent();
                        if (value instanceof HandleNode) {
                            if (((HandleNode) value).isSignal()) {
                                String PVName = ((HandleNode) value).getSignalName();
                                myChannel = ((HandleNode) value).getChannel();
                                jText.setText(null);
                                jText.setText(PVName);
                                selectedPVName = PVName;
                                if (treeSelectionListenerYes) {
                                    if (actionListenerProxy != null) {
                                        actionListenerProxy.actionPerformed(actionEvent);
                                    }
                                }
                            }
                        }
                    } else if (e.getClickCount() == 2) {
                        if (!treeSelectionListenerYes) {
                            if (actionListenerProxy != null) {
                                Object value = selPath.getLastPathComponent();
                                if (value instanceof HandleNode) {
                                    if (((HandleNode) value).isSignal()) {
                                        actionListenerProxy.actionPerformed(actionEvent);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        };
        tree.addMouseListener(ml);
    }
