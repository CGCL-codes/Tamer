    public void dragExit(DragSourceEvent event) {
        if (alpha || reorder) return;
        DragSourceContext dsc = event.getDragSourceContext();
        if (Platform.isPlatformUnix()) dsc.setCursor(Dispatcher.getCursorForAction(-1));
        dragSourceContext = dsc;
        Component parent = getParent();
        if (!(parent instanceof JViewport)) {
            stopScroll();
            return;
        }
        JViewport viewport = (JViewport) parent;
        Rectangle rect = viewport.getViewRect();
        int x = event.getX();
        int y = event.getY();
        Point p = getLocationOnScreen();
        x -= p.x + rect.x;
        y -= p.y + rect.y;
        int rowFudge = (getRowBounds(0).height + 1) / 2;
        int yDiff = getSize().height - rect.height - rect.y;
        if (x < 0 || x >= rect.width || (y < 0 && rect.y < rowFudge) || (y > 0 && yDiff < rowFudge)) {
            stopScroll();
            return;
        }
        if (y < 0) {
            scrollY = (y - 1) / 2;
        } else {
            y -= rect.height;
            scrollY = (y + 2) / 2;
        }
        startScroll();
    }
