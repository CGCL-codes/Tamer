    @Override
    protected void initializeGraphicalViewer() {
        GraphicalViewer viewer = getGraphicalViewer();
        WhiteboardManager.getInstance().getSXEMessageHandler().addMessageListener(new MessageAdapter() {

            @Override
            public void sxeStateMessageApplied(SXEMessage message, ElementRecord root) {
                updateViewerContents(root);
            }
        });
        viewer.setContents(WhiteboardManager.getInstance().getSXEMessageHandler().getDocumentRecord().getRoot());
        viewer.addDropTargetListener(new TemplateTransferDropTargetListener(viewer) {

            /**
			 * Overridden by the superclass method because selecting the created
			 * object here does not make sense as it differs from the one that
			 * will be created by the command (and finally by the DocumentRecord
			 * as it should be).
			 */
            @Override
            protected void handleDrop() {
                updateTargetRequest();
                updateTargetEditPart();
                if (getTargetEditPart() != null) {
                    Command command = getCommand();
                    if (command != null && command.canExecute()) getViewer().getEditDomain().getCommandStack().execute(command); else getCurrentEvent().detail = DND.DROP_NONE;
                } else getCurrentEvent().detail = DND.DROP_NONE;
            }
        });
        super.initializeGraphicalViewer();
    }
