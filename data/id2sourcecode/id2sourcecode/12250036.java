    private BIHNode createNode(int l, int r, BoundingBox nodeBbox, int depth) {
        if ((r - l) < maxTrisPerNode || depth > MAX_TREE_DEPTH) {
            return new BIHNode(l, r);
        }
        BoundingBox currentBox = createBox(l, r);
        Vector3f exteriorExt = nodeBbox.getExtent(null);
        Vector3f interiorExt = currentBox.getExtent(null);
        exteriorExt.subtractLocal(interiorExt);
        int axis = 0;
        if (exteriorExt.x > exteriorExt.y) {
            if (exteriorExt.x > exteriorExt.z) {
                axis = 0;
            } else {
                axis = 2;
            }
        } else {
            if (exteriorExt.y > exteriorExt.z) {
                axis = 1;
            } else {
                axis = 2;
            }
        }
        if (exteriorExt.equals(Vector3f.ZERO)) {
            axis = 0;
        }
        float split = currentBox.getCenter().get(axis);
        int pivot = sortTriangles(l, r, split, axis);
        if (pivot == l || pivot == r) {
            pivot = (r + l) / 2;
        }
        if (pivot < l) {
            BoundingBox rbbox = new BoundingBox(currentBox);
            setMinMax(rbbox, true, axis, split);
            return createNode(l, r, rbbox, depth + 1);
        } else if (pivot > r) {
            BoundingBox lbbox = new BoundingBox(currentBox);
            setMinMax(lbbox, false, axis, split);
            return createNode(l, r, lbbox, depth + 1);
        } else {
            BIHNode node = new BIHNode(axis);
            BoundingBox lbbox = new BoundingBox(currentBox);
            setMinMax(lbbox, false, axis, split);
            node.setLeftPlane(getMinMax(createBox(l, max(l, pivot - 1)), false, axis));
            node.setLeftChild(createNode(l, max(l, pivot - 1), lbbox, depth + 1));
            BoundingBox rbbox = new BoundingBox(currentBox);
            setMinMax(rbbox, true, axis, split);
            node.setRightPlane(getMinMax(createBox(pivot, r), true, axis));
            node.setRightChild(createNode(pivot, r, rbbox, depth + 1));
            return node;
        }
    }
