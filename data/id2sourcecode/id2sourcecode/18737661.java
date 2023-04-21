    private GraphicalViewer getRulerContainer(int orientation) {
        GraphicalViewer result = null;
        switch(orientation) {
            case PositionConstants.NORTH:
                result = top;
                break;
            case PositionConstants.WEST:
                result = left;
        }
        return result;
    }
