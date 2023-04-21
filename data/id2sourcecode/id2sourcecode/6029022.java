    @Override
    public void mouseDragged(MouseEvent e) {
        component.setCursor(handCursor);
        scrollTimer.stop();
        yMouseDragged = e.getPoint().y;
        timeAtMouseDragged = System.currentTimeMillis();
        int meanStopY = (yMouseDragged + yPreviousMouseDragged) / 2;
        scrollUp(meanStopY - yPreviousScroll);
        if (timeAtMouseDragged != timeAtPreviousMouseDragged) {
            double timeIntervalInSeconds = (timeAtMouseDragged - timeAtPreviousMouseDragged) / 1000.0;
            double distanceScrolled = (double) (meanStopY - yPreviousScroll);
            lastMeasuredDragSpeed = distanceScrolled / timeIntervalInSeconds;
        }
        yPreviousMouseDragged = yMouseDragged;
        timeAtPreviousMouseDragged = timeAtMouseDragged;
        yPreviousScroll = meanStopY;
        mouseDragged = true;
    }
