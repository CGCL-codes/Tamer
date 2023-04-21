    protected void positionComponentInCell(Component comp, int x, int y, int cellWidth, int cellHeight) {
        int componentWidth;
        int componentHeight;
        if (fill == VERTICAL || fill == NONE) {
            componentWidth = (int) comp.getPreferredSize().getWidth();
        } else {
            componentWidth = cellWidth;
        }
        if (fill == HORIZONTAL || fill == NONE) {
            componentHeight = (int) comp.getPreferredSize().getHeight();
        } else {
            componentHeight = cellHeight;
        }
        int xAnchor, yAnchor;
        if (fill == BOTH || fill == HORIZONTAL || anchor == NORTHWEST || anchor == WEST || anchor == SOUTHWEST) {
            xAnchor = x;
        } else if (anchor == NORTHEAST || anchor == EAST || anchor == SOUTHEAST) {
            xAnchor = x + cellWidth - componentWidth;
        } else {
            xAnchor = x + (cellWidth - componentWidth) / 2;
        }
        if (fill == BOTH || fill == VERTICAL || anchor == NORTH || anchor == NORTHWEST || anchor == NORTHEAST) {
            yAnchor = y;
        } else if (anchor == SOUTHEAST || anchor == SOUTH || anchor == SOUTHWEST) {
            yAnchor = y + cellHeight - componentHeight;
        } else {
            yAnchor = y + (cellHeight - componentHeight) / 2;
        }
        comp.setBounds(xAnchor, yAnchor, componentWidth, componentHeight);
    }
