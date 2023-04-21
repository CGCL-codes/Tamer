    public CoordinateSequence2D(double... xy) {
        if (xy == null) {
            x = new double[0];
            y = new double[0];
        } else {
            if (xy.length % 2 != 0) {
                throw new IllegalArgumentException("xy must have an even number of values");
            }
            x = new double[xy.length / 2];
            y = new double[xy.length / 2];
            for (int i = 0, k = 0; k < xy.length; i++, k += 2) {
                x[i] = xy[k];
                y[i] = xy[k + 1];
            }
        }
    }
