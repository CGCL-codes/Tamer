    private void insertHelper(ForceItem p, QuadTreeNode n, float x1, float y1, float x2, float y2) {
        float x = p.location[0], y = p.location[1];
        float splitx = (x1 + x2) / 2;
        float splity = (y1 + y2) / 2;
        int i = (x >= splitx ? 1 : 0) + (y >= splity ? 2 : 0);
        if (n.children[i] == null) {
            n.children[i] = factory.getQuadTreeNode();
            n.hasChildren = true;
        }
        if (i == 1 || i == 3) {
            x1 = splitx;
        } else {
            x2 = splitx;
        }
        if (i > 1) {
            y1 = splity;
        } else {
            y2 = splity;
        }
        insert(p, n.children[i], x1, y1, x2, y2);
    }
