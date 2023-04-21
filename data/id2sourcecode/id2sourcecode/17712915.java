    private void initGraphicalViewer() {
        graphicalViewer.addDropTargetListener(new EnvironmentTransferDropTargetListener(graphicalViewer, EnvironmentItemTransfer.getInstance()));
        graphicalViewer.addDropTargetListener(new RoleTransferDropTargetListener(graphicalViewer, RoleItemTransfer.getInstance()));
        graphicalViewer.addDropTargetListener(new InteractionOperationTransferDropTargetListener(graphicalViewer, InteractionOperationItemTransfer.getInstance()));
        graphicalViewer.addDropTargetListener((TransferDropTargetListener) new TemplateTransferDropTargetListener(graphicalViewer));
        graphicalViewer.getControl().addMouseListener(new MouseListener() {

            public void mouseDoubleClick(MouseEvent e) {
                EditPart part = graphicalViewer.findObjectAt(new Point(e.x, e.y));
                if (part instanceof ActivityEditPart) ((ActivityEditPart) part).openEditDialog();
                if (part instanceof TextNodeEditPart) {
                    ((TextNodeEditPart) part).startDoubleClickEding();
                }
            }

            public void mouseDown(MouseEvent e) {
            }

            public void mouseUp(MouseEvent e) {
            }
        });
    }
