    public static Tuple<Float, Integer>[] all_maxima_and_location(float[] R, float min_valley) throws UnexpectedResultException {
        Vector<Tuple<Float, Integer>> filtered = new Vector<Tuple<Float, Integer>>();
        int h = 1;
        while (h < R.length - 1) {
            if (R[h] > R[h - 1] || h == 1) {
                int pk = h;
                while (h + 2 < R.length && FloatingPoint.are_values_equal(R[h], R[h + 1])) {
                    h++;
                }
                if (h != pk) {
                    if (R[h] > R[h + 1]) {
                        if (pk == 1 && FloatingPoint.are_values_equal(R[0], R[1])) {
                            pk = Math.round((float) (pk - 1 + h) / 2);
                        } else {
                            pk = (pk + h) / 2;
                        }
                        filtered.add(new Tuple<Float, Integer>(R[pk], pk));
                    } else if (h + 2 == R.length) {
                        if (R[h] < R[h + 1]) {
                            pk = h + 1;
                        } else if (FloatingPoint.are_values_equal(R[h], R[h + 1])) {
                            pk = (int) ((float) (pk + h + 1) / 2);
                        } else {
                            pk = (pk + h) / 2;
                        }
                        filtered.add(new Tuple<Float, Integer>(R[pk], pk));
                    }
                } else {
                    if (R[h] > R[h + 1]) {
                        filtered.add(new Tuple<Float, Integer>(R[pk], pk));
                    }
                }
            }
            h++;
        }
        if (filtered.size() < 1) {
            filtered.add(new Tuple<Float, Integer>(R[R.length - 1], R.length - 1));
        }
        Tuple<Float, Integer>[] X = separate_peaks(filtered, R, min_valley);
        return X;
    }
