    public Location centerOnRegion(Camera cam, double x1, double y1, double x2, double y2) {
        if (!this.ownsCamera(cam)) {
            throw new IllegalArgumentException("this view does not own Camera 'cam'");
        }
        double west = Math.min(x1, x2);
        double north = Math.max(y1, y2);
        double east = Math.max(x1, x2);
        double south = Math.min(y1, y2);
        double newX = (west + east) / 2;
        double newY = (north + south) / 2;
        Dimension viewSize = getSize();
        double nah = (east - newX) * 2 * cam.getFocal() / viewSize.width - cam.getFocal();
        double nav = (north - newY) * 2 * cam.getFocal() / viewSize.height - cam.getFocal();
        double newAlt = Math.max(nah, nav);
        return new Location(newX, newY, newAlt);
    }
