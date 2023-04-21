    public void showSelectedNodes() {
        MindMapNode selected = mMindMapController.getSelected();
        List selecteds = mMindMapController.getSelecteds();
        if (selecteds.size() == 1) {
            MapNodePositionHolder hook = MapNodePositionHolder.getHook(selected);
            if (hook != null) {
                showNode(hook);
            }
            return;
        }
        int x_min = Integer.MAX_VALUE;
        int y_min = Integer.MAX_VALUE;
        int x_max = Integer.MIN_VALUE;
        int y_max = Integer.MIN_VALUE;
        int mapZoomMax = getMaxZoom();
        for (Iterator it = selecteds.iterator(); it.hasNext(); ) {
            MindMapNode node = (MindMapNode) it.next();
            MapNodePositionHolder hook = MapNodePositionHolder.getHook(node);
            if (hook != null) {
                int x = OsmMercator.LonToX(hook.getPosition().getLon(), mapZoomMax);
                int y = OsmMercator.LatToY(hook.getPosition().getLat(), mapZoomMax);
                x_max = Math.max(x_max, x);
                y_max = Math.max(y_max, y);
                x_min = Math.min(x_min, x);
                y_min = Math.min(y_min, y);
                if (node == selected) {
                    getMap().setCursorPosition(hook.getPosition());
                    changeTileSource(hook.getTileSource(), map);
                }
            }
        }
        int height = Math.max(0, getMap().getHeight());
        int width = Math.max(0, getMap().getWidth());
        int newZoom = mapZoomMax;
        int x = x_max - x_min;
        int y = y_max - y_min;
        while (x > width || y > height) {
            newZoom--;
            x >>= 1;
            y >>= 1;
        }
        x = x_min + (x_max - x_min) / 2;
        y = y_min + (y_max - y_min) / 2;
        int z = 1 << (mapZoomMax - newZoom);
        x /= z;
        y /= z;
        getMap().setDisplayPosition(x, y, newZoom);
    }
