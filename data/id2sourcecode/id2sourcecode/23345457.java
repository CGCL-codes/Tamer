    private void drawInBetweenMarkers(DrawContext dc, int im1, Vec4 p1, double r1, int im2, Vec4 p2, double r2, List<Marker> markerList, Layer parentLayer, Vec4 eyePoint) {
        if (im2 == im1 + 1) return;
        if (p1.distanceTo3(p2) <= r1 + r2) return;
        int im = (im1 + im2) / 2;
        Marker m = markerList.get(im);
        Vec4 p = this.computeSurfacePoint(dc, m.getPosition());
        double r = this.computeMarkerRadius(dc, p, m);
        boolean b1 = false, b2 = false;
        if (p.distanceTo3(p1) > r + r1) {
            this.drawInBetweenMarkers(dc, im1, p1, r1, im, p, r, markerList, parentLayer, eyePoint);
            b1 = true;
        }
        if (p.distanceTo3(p2) > r + r2) {
            this.drawInBetweenMarkers(dc, im, p, r, im2, p2, r2, markerList, parentLayer, eyePoint);
            b2 = true;
        }
        if (b1 && b2 && this.intersectsFrustum(dc, p, r)) dc.addOrderedRenderable(new OrderedMarker(im, m, p, r, parentLayer, eyePoint.distanceTo3(p)));
    }
