    private void storeLayout() {
        for (Iterator it = hierarchicalGraph.nodes.iterator(); it.hasNext(); ) {
            HierarchicalNode n = (HierarchicalNode) it.next();
            if (n.underlying == null) {
                continue;
            }
            if (orientation == PARENT_TOP) {
                n.underlying.setLocation(n.location, n.level.location);
            } else {
                n.underlying.setLocation(n.level.location, n.location);
            }
        }
        for (Iterator it = originalEdges.iterator(); it.hasNext(); ) {
            HierarchicalEdge e = (HierarchicalEdge) it.next();
            GeneralPath shape = new GeneralPath();
            for (int i = 0; i < e.componentEdges.size(); i++) {
                HierarchicalEdge edge = (HierarchicalEdge) e.componentEdges.get(i);
                HierarchicalNode parent = (HierarchicalNode) edge.getParent();
                HierarchicalNode child = (HierarchicalNode) edge.getChild();
                int parentLocation = parent.location;
                int childLocation = child.location;
                int levelParent = parent.level.location + parent.level.depth / 2;
                int levelChild = child.level.location - child.level.depth / 2;
                int levelCentre = (levelParent + levelChild) / 2;
                int nodeParent = parent.level.location + parent.betweenLevelSize / 2;
                int nodeChild = child.level.location - child.betweenLevelSize / 2;
                if (orientation == PARENT_TOP) {
                    shape.moveTo(parentLocation, nodeParent);
                    shape.lineTo(parentLocation, levelParent);
                    shape.curveTo(parentLocation, levelCentre, childLocation, levelCentre, childLocation, levelChild);
                    shape.lineTo(childLocation, nodeChild);
                } else {
                    shape.moveTo(nodeParent, parentLocation);
                    shape.lineTo(levelParent, parentLocation);
                    shape.curveTo(levelCentre, parentLocation, levelCentre, childLocation, levelChild, childLocation);
                    shape.lineTo(nodeChild, childLocation);
                }
            }
            e.underlying.setRoute(shape);
        }
    }
