    private void setRulerContainer(GraphicalViewer container, int orientation) {
        if (orientation == PositionConstants.NORTH) {
            if (top == container) return;
            disposeRulerViewer(top);
            top = container;
        } else if (orientation == PositionConstants.WEST) {
            if (left == container) return;
            disposeRulerViewer(left);
            left = container;
        }
    }
