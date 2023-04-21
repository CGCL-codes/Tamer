    private float compare_mode(double slope, double intercept, int above, float alpha) {
        double percentages = 0;
        float min_focus_x = FOCUS_MIN;
        float max_focus_x = FOCUS_MAX;
        float ax = (max_focus_x - min_focus_x) / 2;
        for (int x = 1; x <= ITERATIONS; x++) {
            int s_inz = 0;
            int c_inz = 0;
            double ay = slope * ax;
            for (PeakPairIdx ppi : comp.get_array()) {
                float s = ppi.get_height_1();
                float c = ppi.get_height_2();
                if (above_line(slope, intercept, s, c)) {
                    if (in_zone_top(slope, ay, s, c)) {
                        c_inz += 1;
                    }
                } else {
                    if (in_zone_bottom(slope, ax, s, c)) {
                        s_inz += 1;
                    }
                }
            }
            percentages = (float) c_inz / above;
            LB.notice("zone: " + x + "\tc_inz: " + c_inz + "\ts_inz: " + s_inz + "\test. fp.: " + Utilities.DecimalPoints(percentages * s_inz, 1) + "\t%: " + Utilities.DecimalPoints(percentages * FPConstants.PERCENTAGE, 2));
            if (percentages < alpha) {
                LB.debug("max_focus_x = ax;\t" + max_focus_x + "\t" + ax);
                max_focus_x = ax;
            } else if (percentages > alpha) {
                LB.debug("min_focus_x = ax;\t" + min_focus_x + "\t" + ax);
                min_focus_x = ax;
            }
            ax = (min_focus_x + max_focus_x) / 2;
        }
        return ax;
    }
