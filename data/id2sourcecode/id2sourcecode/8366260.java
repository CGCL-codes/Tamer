    private static double tuneWeek(double ret) {
        final double blend = 0.4;
        if ((int) Math.round(ret) == 6 || (int) Math.round(ret) == 8) {
            ret = (ret * blend + 7 * (1 - blend));
        }
        if (Math.round(ret) == 13 || Math.round(ret) == 15) {
            ret = (ret + 14) / 2;
        }
        return ret;
    }
