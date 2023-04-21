    private void positionComponent(final Entry entry, final int x, final int y, final int width, final int height) {
        final TableLayoutConstraints constraints = entry.getConstraints();
        final Dimension maxSize;
        final Dimension minSize = getComponentPreferredSize(entry.getComponent());
        int newWidth;
        int newHeight;
        final int newX;
        final int newY;
        if (constraints.getObeyMaximumSize() == true) {
            maxSize = getComponentMaximumSize(entry.getComponent());
        } else {
            maxSize = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }
        if (constraints.getVerticalStretch()) {
            newHeight = Math.min(maxSize.height, height);
        } else {
            newHeight = minSize.height;
        }
        if (constraints.getHorizontalStretch()) {
            newWidth = Math.min(maxSize.width, width);
        } else {
            newWidth = minSize.width;
        }
        if (newHeight > height) {
            newHeight = height;
        }
        if (newWidth > width) {
            newWidth = width;
        }
        switch(constraints.getVerticalAlignment()) {
            case TOP:
                newY = y;
                break;
            case BOTTOM:
                newY = y + (height - newHeight);
                break;
            case CENTER:
                newY = y + (height - newHeight) / 2;
                break;
            default:
                throw new IllegalStateException("Illegal value for verticalAlignment: " + constraints.getVerticalAlignment());
        }
        switch(constraints.getHorizontalAlignment()) {
            case LEFT:
                newX = x;
                break;
            case RIGHT:
                newX = x + (width - newWidth);
                break;
            case CENTER:
                newX = x + (width - newWidth) / 2;
                break;
            default:
                throw new IllegalStateException("Illegal value for horizontalAlignment: " + constraints.getVerticalAlignment());
        }
        entry.getComponent().setBounds(newX, newY, newWidth, newHeight);
    }
