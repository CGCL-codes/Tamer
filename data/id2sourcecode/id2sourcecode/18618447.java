    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Set getSelectableEditParts(GraphicalViewer viewer) {
        HashSet selectableChildren = new HashSet();
        for (Object child : viewer.getContents().getChildren()) {
            if (child instanceof GraphicalEditPart) {
                GraphicalEditPart childPart = (GraphicalEditPart) child;
                if (childPart.isSelectable()) {
                    selectableChildren.add(childPart);
                    for (Object o : childPart.getSourceConnections()) {
                        GraphicalEditPart connectionEditPart = (GraphicalEditPart) o;
                        if (connectionEditPart.isSelectable()) {
                            selectableChildren.add(connectionEditPart);
                        }
                    }
                    for (Object o : childPart.getTargetConnections()) {
                        GraphicalEditPart connectionEditPart = (GraphicalEditPart) o;
                        if (connectionEditPart.isSelectable()) {
                            selectableChildren.add(connectionEditPart);
                        }
                    }
                }
            }
        }
        return selectableChildren;
    }
