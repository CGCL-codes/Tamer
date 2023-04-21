    public void doLayout() {
        levels.clear();
        ArrayList<NodeObj> nodeList = new ArrayList<NodeObj>(nodes.values());
        boolean[] seenem = new boolean[nodes.size()];
        Collections.sort(nodeList, new Comparator<NodeObj>() {

            public int compare(NodeObj o1, NodeObj o2) {
                return objectComparator.compare(o1, o2);
            }
        });
        for (Iterator<NodeObj> i = nodeList.iterator(); i.hasNext(); ) {
            NodeObj n = i.next();
            findLevel(maxLevelSize, n, seenem);
        }
        rationalise();
        for (Iterator<Level> i = levels.iterator(); i.hasNext(); ) {
            Level l = i.next();
            l.calcInitialPositions();
        }
        orderNodesInLevels();
        calcLevelLocations();
        int minStart = Integer.MAX_VALUE;
        for (Iterator<Level> i = levels.iterator(); i.hasNext(); ) minStart = Math.min(minStart, i.next().getStart());
        for (Iterator<Level> i = levels.iterator(); i.hasNext(); ) i.next().shiftLeft(minStart);
        posMap = new HashMap<Object, Shape>();
        edgeShapes = new HashMap<DummyEdge, Shape>();
        int multiplier = 1;
        if (orientation == PARENT_BOTTOM || orientation == PARENT_RIGHT) {
            multiplier = -1;
        }
        for (NodeObj n : nodes.values()) {
            if (n.isDummy()) continue;
            Point2D offset = new Point2D.Double();
            if (orientation == PARENT_TOP || orientation == PARENT_BOTTOM) offset.setLocation(n.getLocation(), multiplier * n.getLevel().location); else offset.setLocation(multiplier * n.getLevel().location, n.getLocation());
            offset.setLocation(offset.getX() - n.getWidth() / 2, offset.getY() - n.getHeight() / 2 - 5);
            Rectangle r = new Rectangle((int) offset.getX(), (int) offset.getY(), n.getWidth(), n.getHeight());
            posMap.put(n.getNode(), r);
        }
        for (DummyEdge n : dummyEdges) {
            if (n.isDummy()) continue;
            GeneralPath shape = new GeneralPath();
            for (DummyEdge edge : n.getComponentEdges()) {
                NodeObj parent = edge.getLayoutParent();
                NodeObj child = edge.getLayoutChild();
                if (child == null) continue;
                int parentLocation = parent.getLocation();
                int childLocation = child.getLocation();
                int levelParent = parent.getLevel().location + parent.getLevel().depth / 2;
                int levelChild = child.getLevel().location - child.getLevel().depth / 2;
                int levelCentre = (levelParent + levelChild) / 2;
                int nodeParent;
                int nodeChild;
                if (drawEdgesToNodeCenters) {
                    nodeParent = parent.getLevel().location;
                    nodeChild = child.getLevel().location;
                } else {
                    nodeParent = parent.getLevel().location + parent.getLayoutHeight() / 2;
                    nodeChild = child.getLevel().location - child.getLayoutHeight() / 2;
                }
                if (orientation == PARENT_TOP || orientation == PARENT_BOTTOM) {
                    if (shape.getCurrentPoint() == null) shape.moveTo(parentLocation, multiplier * nodeParent); else shape.lineTo(parentLocation, multiplier * nodeParent);
                    shape.lineTo(parentLocation, multiplier * levelParent);
                    shape.curveTo(parentLocation, multiplier * levelCentre, childLocation, multiplier * levelCentre, childLocation, multiplier * levelChild);
                    shape.lineTo(childLocation, multiplier * nodeChild);
                } else {
                    if (shape.getCurrentPoint() == null) shape.moveTo(multiplier * nodeParent, parentLocation); else shape.lineTo(multiplier * nodeParent, parentLocation);
                    shape.lineTo(multiplier * levelParent, parentLocation);
                    shape.curveTo(multiplier * levelCentre, parentLocation, multiplier * levelCentre, childLocation, multiplier * levelChild, childLocation);
                    shape.lineTo(multiplier * nodeChild, childLocation);
                }
            }
            if (orientation == PARENT_TOP || orientation == PARENT_LEFT) shape = (GeneralPath) ShapeUtil.reverseShape(shape, null);
            edgeShapes.put(n, shape);
        }
    }
