        public int currentSegment(double[] coords) {
            int retval;
            if (Double.isNaN(points[i])) {
                coords[0] = points[i + 1];
                coords[1] = points[i + 2];
                retval = SEG_MOVETO;
            } else {
                coords[0] = points[i];
                coords[1] = points[i + 1];
                retval = SEG_LINETO;
            }
            if (transform != null) transform.transform(coords, 0, coords, 0, 1);
            return retval;
        }
