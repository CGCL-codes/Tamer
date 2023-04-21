    void adjustPosition() {
        int xb = m_x;
        int yb = m_y;
        int xr = m_xr;
        int yr = m_yr;
        double d2 = m_d2;
        long t0 = 0;
        long t1 = 3 * m_elapsed;
        if (t1 == 0) t1 = 500;
        long t = t0;
        if (debug) {
            System.out.println("Elapsed = " + m_elapsed + " D2= " + d2 + " searching to " + t1 + " ms ago");
            System.out.println("Ball : " + xb + ", " + yb + ")  going (" + m_coseno + ", " + m_seno + ") with vb= " + m_v);
            System.out.println("Robot: " + xr + ", " + yr + ")  going (" + r_coseno + ", " + r_seno + ") with vr= " + m_vr);
        }
        while (t1 - t0 > 2) {
            t = t0 + (t1 - t0) / 2;
            xb = m_x - (int) Math.floor(m_v * t * m_coseno / 1000);
            yb = m_y - (int) Math.floor(m_v * t * m_seno / 1000);
            xr = m_xr - (int) Math.floor(m_vr * t * r_coseno / 1000);
            yr = m_yr - (int) Math.floor(m_vr * t * r_seno / 1000);
            if (debug) System.out.println("  -->Ball : " + xb + ", " + yb + ")");
            if (debug) System.out.println("  -->Robot: " + xr + ", " + yr + ")");
            d2 = (xb - xr) * (xb - xr) + (yb - yr) * (yb - yr);
            if (debug) System.out.println("  d2 was " + d2 + "  " + t + "  ms ago");
            if (d2 < 12000) t0 = t; else t1 = t;
        }
        m_x = xb;
        m_y = yb;
        if (debug) System.out.println("D2 from " + m_d2 + " to " + d2 + " Impact was " + t + " ms ago. pos corrected from (" + m_x + ", " + m_y + ") to (" + xb + ", " + yb + ")");
        m_elapsed = t;
    }
