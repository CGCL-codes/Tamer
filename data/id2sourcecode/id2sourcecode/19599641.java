    private MouseMode activateMouseMode(MouseEvent e) {
        boolean xLeftSide = false;
        boolean xRightSide = false;
        boolean xMiddle = false;
        boolean yTopSide = false;
        boolean yBottomSide = false;
        boolean yMiddle = false;
        Point mousePoint = e.getPoint();
        mousePoint.translate(parent.getX(), parent.getY());
        if (parent.getRow() != DasRow.NULL && parent.getColumn() != DasColumn.NULL) {
            int xLeft = parent.getColumn().getDMinimum();
            int xRight = parent.getColumn().getDMaximum();
            int yTop = parent.getRow().getDMinimum();
            int yBottom = parent.getRow().getDMaximum();
            int xmid = (xLeft + xRight) / 2;
            int ymid = (yTop + yBottom) / 2;
            xLeftSide = mousePoint.getX() < xLeft + 10;
            xRightSide = mousePoint.getX() > xRight - 10;
            xMiddle = Math.abs(mousePoint.getX() - xmid) < 4;
            yTopSide = (mousePoint.getY() < yTop + 10) && (mousePoint.getY() >= yTop);
            yBottomSide = mousePoint.getY() > (yBottom - 10);
            yMiddle = Math.abs(mousePoint.getY() - ymid) < 4;
        }
        MouseMode result = MouseMode.idle;
        Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        if (!(parent instanceof DasAxis)) {
            if ((e.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK) == MouseEvent.SHIFT_DOWN_MASK) {
                if (xLeftSide) {
                    if (yTopSide) {
                        result = MouseMode.resize;
                        cursor = new Cursor(Cursor.NW_RESIZE_CURSOR);
                    } else if (yBottomSide) {
                        result = MouseMode.resize;
                        cursor = new Cursor(Cursor.SW_RESIZE_CURSOR);
                    } else if (yMiddle) {
                        result = MouseMode.resize;
                        cursor = new Cursor(Cursor.W_RESIZE_CURSOR);
                    }
                } else if (xRightSide) {
                    if (yTopSide) {
                        result = MouseMode.resize;
                        cursor = new Cursor(Cursor.NE_RESIZE_CURSOR);
                    } else if (yBottomSide) {
                        result = MouseMode.resize;
                        cursor = new Cursor(Cursor.SE_RESIZE_CURSOR);
                    } else if (yMiddle) {
                        result = MouseMode.resize;
                        cursor = new Cursor(Cursor.E_RESIZE_CURSOR);
                    }
                } else if (xMiddle && yMiddle) {
                    result = MouseMode.move;
                    cursor = new Cursor(Cursor.MOVE_CURSOR);
                } else if (xMiddle && yTopSide) {
                    result = MouseMode.resize;
                    cursor = new Cursor(Cursor.N_RESIZE_CURSOR);
                } else if (xMiddle && yBottomSide) {
                    result = MouseMode.resize;
                    cursor = new Cursor(Cursor.S_RESIZE_CURSOR);
                }
            }
        }
        if (result == MouseMode.resize) {
            result.resizeBottom = yBottomSide;
            result.resizeTop = yTopSide;
            result.resizeRight = xRightSide;
            result.resizeLeft = xLeftSide;
        } else if (result == MouseMode.move) {
            result.moveStart = e.getPoint();
            result.moveStart.translate(-parent.getX(), -parent.getY());
        }
        if (result != mouseMode) {
            getGlassPane().setCursor(cursor);
        }
        return result;
    }
