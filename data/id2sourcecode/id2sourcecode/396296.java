    public void mouseReleaseAction(MouseEvent mevt, main_canvas theCanvas) {
        int endX = (int) (mevt.getX());
        int endY = (int) (mevt.getY());
        if (endX != x && endY != y) dragged = true;
        if (!dragged) {
            double currentZoom = theCanvas.getZoom();
            if (SwingUtilities.isLeftMouseButton(mevt)) {
                if (currentZoom < 8.0) {
                    theCanvas.setZoom(currentZoom * 2.0);
                    theCanvas.setOldZoom(currentZoom);
                }
            } else {
                if (currentZoom > 1.0) {
                    theCanvas.setZoom(currentZoom * 0.5);
                    theCanvas.setOldZoom(currentZoom);
                }
            }
            theZoom = theCanvas.getZoom();
            Point focus = new Point((int) (x * theZoom), (int) (y * theZoom));
            theCanvas.pictureScrollPane.getViewport().setExtentSize(new Dimension(theCanvas.getBufferedImage().getWidth(), theCanvas.getBufferedImage().getHeight()));
            theCanvas.pictureScrollPane.getViewport().setViewSize(new Dimension((int) theZoom, (int) theZoom));
            theCanvas.pictureScrollPane.getViewport().setViewPosition(theCanvas.pictureScrollPane.getViewport().toViewCoordinates(focus));
            System.out.println(x + " " + y + " is " + theCanvas.pictureScrollPane.getViewport().toViewCoordinates(focus));
            theCanvas.repaint();
        } else {
            theZoom = theCanvas.getZoom();
            int centerX = (x + endX) / 2;
            int centerY = (y + endY) / 2;
            double zoomFactorX = (endX - x);
            if (zoomFactorX < 0) zoomFactorX = (x - endX);
            double zoomFactorY = (endY - y);
            if (zoomFactorY < 0) zoomFactorY = (y - endY);
            double width = theCanvas.pictureScrollPane.getViewport().getExtentSize().getWidth();
            double height = theCanvas.pictureScrollPane.getViewport().getExtentSize().getHeight();
            double zoomFactor = width / zoomFactorX;
            double check = height / zoomFactorY;
            if (check < zoomFactor) zoomFactor = check;
            double currentZoom = theCanvas.getZoom();
            zoomFactor = (int) zoomFactor;
            theCanvas.setZoom(zoomFactor);
            System.out.println("zoomFactor to: " + zoomFactor);
            theCanvas.setOldZoom(currentZoom);
            theZoom = theCanvas.getZoom();
            int myX = x;
            if (endX < x) myX = endX;
            int myY = y;
            if (endY < y) myY = endY;
            Point focus = new Point((int) (myX * theZoom), (int) (myY * theZoom));
            theCanvas.pictureScrollPane.getViewport().setExtentSize(new Dimension(theCanvas.getBufferedImage().getWidth(), theCanvas.getBufferedImage().getHeight()));
            theCanvas.pictureScrollPane.getViewport().setViewSize(new Dimension((int) zoomFactorX, (int) zoomFactorY));
            theCanvas.pictureScrollPane.getViewport().setViewPosition(theCanvas.pictureScrollPane.getViewport().toViewCoordinates(focus));
            System.out.println(theCanvas.pictureScrollPane.getViewport().getViewPosition().getX() + " " + theCanvas.pictureScrollPane.getViewport().getViewPosition().getY());
            if (SwingUtilities.isLeftMouseButton(mevt) || theCanvas.zoomFactor >= 1) {
                if (theCanvas.main_image.getWidth() * theCanvas.zoomFactor * theCanvas.main_image.getHeight() * theCanvas.zoomFactor < 10000000) theCanvas.repaint(); else theCanvas.zoomFactor = theCanvas.oldZoomFactor;
            }
        }
    }
