    public void run() {
        Command result = null;
        List selection = getSelectedObjects();
        if (selection != null) {
            Object obj = selection.get(0);
            if (obj instanceof GraphicalEditPart) {
                GraphicalEditPart gep = (GraphicalEditPart) obj;
                ChangeBoundsRequest request = new ChangeBoundsRequest();
                TreeMap<String, Integer> map = new TreeMap<String, Integer>();
                GraphicalViewer shape = (GraphicalViewer) this.editor.getAdapter(GraphicalViewer.class);
                System.out.println(shape.getContents().getClass());
                PageDiagram root = (PageDiagram) (shape.getContents().getModel());
                System.out.println(root.getChildren().size() - 1);
                map.put("zIndex", root.getChildren().size() - 1);
                request.setExtendedData(map);
                request.setEditParts(selection);
                request.setType(RequestConstants.REQ_MOVE_CHILDREN);
                result = gep.getCommand(request);
                execute(result);
            }
        }
    }
