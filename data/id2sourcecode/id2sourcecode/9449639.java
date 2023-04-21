    public static Tuple<Integer, Integer>[] all_maxima_and_location(int[] R, float min_valley) throws UnexpectedResultException {
        Vector<Tuple<Integer, Integer>> filtered = new Vector<Tuple<Integer, Integer>>();
        int h = 1;
        while (h < R.length - 1) {
            if (R[h] > R[h - 1] || h == 1) {
                int pk = h;
                while (h + 2 < R.length && R[h] == R[h + 1]) {
                    h++;
                }
                if (h != pk) {
                    if (R[h] > R[h + 1]) {
                        if (pk == 1 && R[0] == R[1]) {
                            pk = Math.round((float) (pk - 1 + h) / 2);
                        } else {
                            pk = (pk + h) / 2;
                        }
                        filtered.add(new Tuple<Integer, Integer>(R[pk], pk));
                    } else if (h + 2 == R.length) {
                        if (R[h] < R[h + 1]) {
                            pk = h + 1;
                        } else if (R[h] == R[h + 1]) {
                            pk = (int) ((float) (pk + h + 1) / 2);
                        } else {
                            pk = (pk + h) / 2;
                        }
                        filtered.add(new Tuple<Integer, Integer>(R[pk], pk));
                    }
                } else {
                    if (R[h] > R[h + 1]) {
                        filtered.add(new Tuple<Integer, Integer>(R[pk], pk));
                    }
                }
            }
            h++;
        }
        if (filtered.size() < 1) {
            filtered.add(new Tuple<Integer, Integer>(R[R.length - 1], R.length - 1));
        }
        Tuple<Integer, Integer>[] X = separate_peaks(filtered, R, min_valley);
        return X;
    }
