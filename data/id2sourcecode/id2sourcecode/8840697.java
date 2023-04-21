    public double[] buildSignal(double dt) {
        double level1, level2, period, phase, duration;
        double am, om, fi, um, ii;
        double[] result = null;
        try {
            level1 = ((Double) parameters.get(0)).doubleValue();
            level2 = ((Double) parameters.get(1)).doubleValue();
            period = ((Double) parameters.get(2)).doubleValue();
            phase = ((Double) parameters.get(3)).doubleValue();
            duration = ((Double) parameters.get(4)).doubleValue();
            if ((duration <= 0) || (period <= 0)) return result;
            duration = duration / dt;
            int steps = (int) Math.round(duration);
            result = new double[steps];
            int count = 0;
            am = (level1 + level2) / 2;
            um = (level2 - level1) / 2;
            om = 2 * Math.PI * dt / period;
            fi = phase * 2 * Math.PI / 360;
            ii = 0;
            while (count < steps) {
                level1 = am + um * Math.sin(om * ii + fi);
                result[count] = level1;
                ii++;
                count++;
            }
        } catch (Exception e) {
        }
        return result;
    }
