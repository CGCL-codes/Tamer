    public ShapesReference(ShapeRecords sr_) {
        sr = sr_;
        boundingbox_all = new double[2][2];
        mask_dimension = (int) Math.sqrt(sr.getNumberOfRecords());
        if (mask_dimension < 3) {
            mask = null;
            return;
        }
        mask = new ArrayList[mask_dimension][mask_dimension];
        boolean first_time = true;
        for (ComplexRegion s : sr_.getRegions()) {
            double[][] bb = s.getBoundingBox();
            if (first_time || boundingbox_all[0][0] > bb[0][0]) {
                boundingbox_all[0][0] = bb[0][0];
            }
            if (first_time || boundingbox_all[1][0] < bb[1][0]) {
                boundingbox_all[1][0] = bb[1][0];
            }
            if (first_time || boundingbox_all[0][1] > bb[0][1]) {
                boundingbox_all[0][1] = bb[0][1];
            }
            if (first_time || boundingbox_all[1][1] < bb[1][1]) {
                boundingbox_all[1][1] = bb[1][1];
            }
            first_time = false;
        }
        ArrayList<ComplexRegion> sra = sr.getRegions();
        for (int j = 0; j < sra.size(); j++) {
            ComplexRegion s = sra.get(j);
            int[][] map = s.getOverlapGridCells_Box(boundingbox_all[0][0], boundingbox_all[0][1], boundingbox_all[1][0], boundingbox_all[1][1], mask_dimension, mask_dimension, s.getBoundingBox(), null, false);
            for (int i = 0; i < map.length; i++) {
                if (mask[map[i][0]][map[i][1]] == null) {
                    mask[map[i][0]][map[i][1]] = new ArrayList<Integer>();
                }
                mask[map[i][0]][map[i][1]].add(j);
            }
        }
        mask_long_multiplier = mask_dimension / (double) (boundingbox_all[1][0] - boundingbox_all[0][0]);
        mask_lat_multiplier = mask_dimension / (double) (boundingbox_all[1][1] - boundingbox_all[0][1]);
    }
