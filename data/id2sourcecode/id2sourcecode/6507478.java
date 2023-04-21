    void pickArea(int x, int y, boolean add) {
        PickCanvas pickCanvas = new PickCanvas(_canvas, this._universe.getLocale());
        pickCanvas.setMode(PickTool.GEOMETRY_INTERSECT_INFO);
        int cx = (_pickX + x) / 2;
        int cy = (_pickY + y) / 2;
        float t = Math.max(Math.abs(_pickX - x), Math.abs(_pickY - y)) / 2.f;
        pickCanvas.setTolerance(t);
        pickCanvas.setShapeLocation(cx, cy);
        if (t > 2.) {
            PickResult[] result = pickCanvas.pickAll();
            if (result == null) {
                select(add, new Node[0]);
            } else {
                Node[] n = new Node[result.length];
                int i = 0;
                for (PickResult r : result) {
                    n[i++] = r.getObject();
                }
                select(add, n);
            }
        } else {
            PickResult result = pickCanvas.pickClosest();
            if (result == null) {
                select(add, new Node[0]);
            } else {
                select(add, result.getObject());
            }
        }
    }
