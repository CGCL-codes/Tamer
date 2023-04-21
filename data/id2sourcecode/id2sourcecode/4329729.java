    public void update_screen(Rectangle rect) {
        long currenttime = System.currentTimeMillis();
        if (lastCapture != null) {
            Rectangle bounds = (Rectangle) lastCapture.getBounds().clone();
            bounds.translate(lastCaptureOffset.x, lastCaptureOffset.y);
            if (currenttime - lastScreenCapTime <= maxDelay || bounds.contains(rect)) {
                return;
            }
        }
        lastCapture = r.createScreenCapture(rect).getRaster();
        lastScreenCapTime = currenttime;
        lastCaptureOffset.x = rect.x;
        lastCaptureOffset.y = rect.y;
    }
