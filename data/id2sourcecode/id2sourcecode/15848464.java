    private void startItUp() {
        selectedHandleNode = null;
        tree.expandPath(new TreePath(tree.getModel().getRoot()));
        tree.setBackground(scrollPane.getBackground());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(signalCellRenderer);
        tree.setFont(fnt);
        scrollPane.setViewportView(tree);
        MouseListener ml = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                selectedHandleNode = null;
                int selRow = tree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                if (selRow != -1) {
                    pvNameJText.setText(null);
                    Object value = selPath.getLastPathComponent();
                    if (value instanceof HandleNode) {
                        if (((HandleNode) value).isSignal()) {
                            selectedHandleNode = (HandleNode) value;
                            String PVName = selectedHandleNode.getChannelName();
                            pvNameJText.setText(PVName);
                        }
                    }
                }
            }
        };
        tree.addMouseListener(ml);
    }
