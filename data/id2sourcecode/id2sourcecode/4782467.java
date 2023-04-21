    protected Set<Node> getSelectedNodes() {
        Set<Node> selectedNodes = new HashSet<Node>();
        ScrollingGraphicalViewer viewer = main.getViewer();
        Iterator selectedObjectsIterator = ((IStructuredSelection) viewer.getSelection()).iterator();
        Set<GraphObject> selectedObjects = new HashSet<GraphObject>();
        while (selectedObjectsIterator.hasNext()) {
            Object model = ((EditPart) selectedObjectsIterator.next()).getModel();
            if (model instanceof Node) {
                Node node = (Node) model;
                if (node instanceof ComplexMember) {
                    node = node.getParents().iterator().next();
                }
                selectedObjects.add(node);
            }
        }
        Set<GraphObject> sourceSet = main.getPathwayGraph().getCorrespOrig(selectedObjects);
        for (GraphObject go : sourceSet) {
            if (go instanceof Node) {
                selectedNodes.add((Node) go);
            }
        }
        return selectedNodes;
    }
