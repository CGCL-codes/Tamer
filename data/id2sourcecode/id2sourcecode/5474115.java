    private void processMovement() {
        int spx = (int) specializationPointViewComponent.getCenter().getX();
        int spy = (int) specializationPointViewComponent.getCenter().getY();
        int tfx = (int) transformerViewComponent.getCenter().getX();
        int tfy = (int) transformerViewComponent.getCenter().getY();
        int spmaxy = specializationPointViewComponent.getMaxY() + 15;
        spy = spmaxy;
        int ymid = (tfy + spy) / 2;
        line_vert1 = new Line2D.Float((float) tfx, ymid, (float) tfx, (float) tfy);
        line_vert2 = new Line2D.Float((float) spx, ymid, (float) spx, (float) spy);
        line_horiz = new Line2D.Float((float) spx, ymid, (float) tfx, ymid);
        int xpoints[] = { spx, spx + 15, spx - 15 };
        int ypoints[] = { spy, spy + 15, spy + 15 };
        poly = new Polygon(xpoints, ypoints, 3);
    }
