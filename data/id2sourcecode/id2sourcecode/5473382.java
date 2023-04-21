    public void addPolygon(SimpleRegion sr) {
        simpleregions.add(sr);
        double[][] bb = sr.getBoundingBox();
        if (simpleregions.size() == 1 || boundingbox_all[0][0] > bb[0][0]) {
            boundingbox_all[0][0] = bb[0][0];
        }
        if (simpleregions.size() == 1 || boundingbox_all[1][0] < bb[1][0]) {
            boundingbox_all[1][0] = bb[1][0];
        }
        if (simpleregions.size() == 1 || boundingbox_all[0][1] > bb[0][1]) {
            boundingbox_all[0][1] = bb[0][1];
        }
        if (simpleregions.size() == 1 || boundingbox_all[1][1] < bb[1][1]) {
            boundingbox_all[1][1] = bb[1][1];
        }
        bounding_box = boundingbox_all;
    }
