    public void run() {
        ScrollingGraphicalViewer viewer = main.getViewer();
        Iterator selectedObjects = ((IStructuredSelection) viewer.getSelection()).iterator();
        root = (CompoundModel) ((ChsRootEditPart) viewer.getRootEditPart().getChildren().get(0)).getModel();
        List nodes = new ArrayList();
        boolean createCompound = true;
        while (selectedObjects.hasNext()) {
            Object editpart = selectedObjects.next();
            if (editpart instanceof ChsNodeEditPart) {
                NodeModel node = ((ChsNodeEditPart) editpart).getNodeModel();
                if (node.getParentModel() == root) {
                    nodes.add(node);
                } else {
                    createCompound = false;
                    break;
                }
            }
        }
        if (createCompound) {
            CreateCompoundFromSelectedCommand command = new CreateCompoundFromSelectedCommand(nodes);
            command.execute();
            main.getHighlightLayer().refreshHighlights();
        } else {
            MessageBox box = new MessageBox(main.getShell(), SWT.ICON_WARNING);
            box.setMessage("All selected nodes should belong to the root graph!");
            box.setText("Chisio");
            box.open();
        }
    }
