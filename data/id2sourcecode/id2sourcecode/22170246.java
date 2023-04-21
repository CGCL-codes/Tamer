    public static void putData(String key, double[] points, ArrayList<QueryField> fields, String metadata) {
        double[] bb = new double[4];
        for (int i = 0; i < points.length; i += 2) {
            if (i == 0 || points[i] < bb[0]) {
                bb[0] = points[i];
            }
            if (i == 0 || points[i] > bb[2]) {
                bb[2] = points[i];
            }
            if (i == 0 || points[i + 1] < bb[1]) {
                bb[1] = points[i + 1];
            }
            if (i == 0 || points[i + 1] > bb[3]) {
                bb[3] = points[i + 1];
            }
        }
        Object[] o = new Object[4];
        o[0] = points;
        o[1] = fields;
        o[2] = bb;
        o[3] = metadata.replace("\n", "_n_").replace("\"", "\\\"").replace("/", "\\/").replace("\\", "\\\\").replace("\t", "\\t");
        addData(key, o);
    }
