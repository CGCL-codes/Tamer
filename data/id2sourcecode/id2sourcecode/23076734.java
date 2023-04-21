    public void center() {
        if (null != ptz) {
            double xminus = (xmax + xmin) / 2;
            double yminus = (ymax + ymin) / 2;
            double zminus = (zmax + zmin) / 2;
            Pt3 pt = new Pt3(xminus, yminus, zminus);
            com.insanityengine.ghia.util.SimpleLogger.info("xmax = " + xmax + " xmin = " + xmin + " xminus = " + xminus);
            com.insanityengine.ghia.util.SimpleLogger.info("ymax = " + ymax + " ymin = " + ymin + " yminus = " + yminus);
            com.insanityengine.ghia.util.SimpleLogger.info("zmax = " + zmax + " zmin = " + zmin + " zminus = " + zminus);
            com.insanityengine.ghia.util.SimpleLogger.info("Center by " + pt);
            for (int i = 0; i < ptz.length; i++) {
                ptz[i].subtract(pt);
            }
            xmin -= xminus;
            xmax -= xminus;
            ymin -= yminus;
            ymax -= yminus;
            zmin -= zminus;
            zmax -= zminus;
        }
    }
