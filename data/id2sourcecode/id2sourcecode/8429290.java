    public OverviewAction(final IWorkbenchPart editPart, GraphicalViewer viewer) {
        super(editPart, AS_PUSH_BUTTON);
        setImageDescriptor(UIType.overview.getImageDescriptor());
        setText(ProcessDesignerMessages.OverviewAction_showOverview);
        setToolTipText(getText());
        myGraphicalViewer = viewer;
        myPopupDialog = new OverviewDialog(myGraphicalViewer);
        editPart.getSite().getPage().addPartListener(new IPartListener() {

            public void partActivated(IWorkbenchPart part) {
                if (part.equals(editPart)) {
                    setActiveEditor((DiagramEditor) part);
                    if (myPopupDialog.isVisible()) {
                        run();
                    }
                }
            }

            public void partBroughtToTop(IWorkbenchPart part) {
            }

            public void partClosed(IWorkbenchPart part) {
            }

            public void partDeactivated(IWorkbenchPart part) {
                if (part.equals(editPart)) {
                    if ((myPopupDialog != null) && myPopupDialog.isOpened()) {
                        myPopupDialog.close();
                    }
                }
            }

            public void partOpened(IWorkbenchPart part) {
            }
        });
    }
